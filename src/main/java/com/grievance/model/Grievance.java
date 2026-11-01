package com.grievance.model;

import java.sql.Timestamp;

public class Grievance {
    private int grievanceId;
    private int studentId;
    private String studentName;
    private String title;
    private String category;
    private String description;
    private String department;
    private Integer assignedTo;
    private String assignedFacultyName;
    private String status;
    private String escalationReason;
    private Timestamp createdAt;

    public int getGrievanceId() { return grievanceId; }
    public void setGrievanceId(int grievanceId) { this.grievanceId = grievanceId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }
    public String getAssignedFacultyName() { return assignedFacultyName; }
    public void setAssignedFacultyName(String assignedFacultyName) { this.assignedFacultyName = assignedFacultyName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEscalationReason() { return escalationReason; }
    public void setEscalationReason(String escalationReason) { this.escalationReason = escalationReason; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
