package com.grievance.dao;

import com.grievance.model.Grievance;
import com.grievance.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrievanceDAO {
    public boolean submit(Grievance g) throws SQLException {
        String sql = "INSERT INTO grievances(student_id,title,category,description,department,status) " +
                "VALUES(?,?,?,?,?,'SUBMITTED')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, g.getStudentId());
            ps.setString(2, g.getTitle());
            ps.setString(3, g.getCategory());
            ps.setString(4, g.getDescription());
            ps.setString(5, g.getDepartment());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) g.setGrievanceId(keys.getInt(1));
                }
                addUpdate(con, g.getGrievanceId(), g.getStudentId(), "SUBMITTED", "Grievance submitted");
                return true;
            }
            return false;
        }
    }

    public List<Grievance> findByStudent(int studentId) throws SQLException {
        String sql = baseSelect() + " WHERE g.student_id=? ORDER BY g.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            return collect(ps);
        }
    }

    public List<Grievance> findAll() throws SQLException {
        String sql = baseSelect() + " ORDER BY g.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return collect(ps);
        }
    }

    public List<Grievance> findAssignedTo(int facultyId) throws SQLException {
        String sql = baseSelect() + " WHERE g.assigned_to=? ORDER BY g.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, facultyId);
            return collect(ps);
        }
    }

    private String baseSelect() {
        return "SELECT g.*, u.name AS student_name, f.name AS faculty_name " +
                "FROM grievances g JOIN users u ON g.student_id=u.user_id " +
                "LEFT JOIN users f ON g.assigned_to=f.user_id";
    }

    private List<Grievance> collect(PreparedStatement ps) throws SQLException {
        List<Grievance> list = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Grievance g = new Grievance();
                g.setGrievanceId(rs.getInt("grievance_id"));
                g.setStudentId(rs.getInt("student_id"));
                g.setStudentName(rs.getString("student_name"));
                g.setTitle(rs.getString("title"));
                g.setCategory(rs.getString("category"));
                g.setDescription(rs.getString("description"));
                g.setDepartment(rs.getString("department"));
                int assigned = rs.getInt("assigned_to");
                g.setAssignedTo(rs.wasNull() ? null : assigned);
                g.setAssignedFacultyName(rs.getString("faculty_name"));
                g.setStatus(rs.getString("status"));
                g.setEscalationReason(rs.getString("escalation_reason"));
                g.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(g);
            }
        }
        return list;
    }

    public boolean assign(int grievanceId, int facultyId, int adminId) throws SQLException {
        String sql = "UPDATE grievances SET assigned_to=?,status='ASSIGNED' " +
                "WHERE grievance_id=? AND status NOT IN ('RESOLVED','CLOSED')";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, facultyId);
                ps.setInt(2, grievanceId);
                int rows = ps.executeUpdate();
                if (rows == 1) addUpdate(con, grievanceId, adminId, "ASSIGNED", "Assigned to faculty ID " + facultyId);
                con.commit();
                return rows == 1;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public boolean updateStatus(int grievanceId, int facultyId, String status, String remarks) throws SQLException {
        if (!List.of("IN_PROGRESS", "PENDING", "RESOLVED").contains(status)) return false;
        String sql = "UPDATE grievances SET status=? WHERE grievance_id=? AND assigned_to=?";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, status);
                ps.setInt(2, grievanceId);
                ps.setInt(3, facultyId);
                int rows = ps.executeUpdate();
                if (rows == 1) addUpdate(con, grievanceId, facultyId, status, remarks);
                con.commit();
                return rows == 1;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    public boolean escalate(int grievanceId, int studentId, String reason) throws SQLException {
        String sql = "UPDATE grievances SET status='ESCALATED',escalation_reason=? " +
                "WHERE grievance_id=? AND student_id=? AND status NOT IN ('RESOLVED','CLOSED','ESCALATED')";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, reason);
                ps.setInt(2, grievanceId);
                ps.setInt(3, studentId);
                int rows = ps.executeUpdate();
                if (rows == 1) addUpdate(con, grievanceId, studentId, "ESCALATED", reason);
                con.commit();
                return rows == 1;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    private void addUpdate(Connection con, int grievanceId, int userId,
                           String status, String remarks) throws SQLException {
        String sql = "INSERT INTO grievance_updates(grievance_id,updated_by,status,remarks) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, grievanceId);
            ps.setInt(2, userId);
            ps.setString(3, status);
            ps.setString(4, remarks);
            ps.executeUpdate();
        }
    }
}
