package com.grievance.dao;

import com.grievance.model.User;
import com.grievance.util.DBConnection;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.sql.*;

public class UserDAO {
    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;

    private String hashPassword(String password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        byte[] hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec).getEncoded();
        spec.clearPassword();
        return ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private boolean verifyPassword(String password, String stored) throws Exception {
        String[] parts = stored.split(":");
        if (parts.length != 3) return false;
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] expected = fromHex(parts[2]);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, expected.length * 8);
        byte[] actual = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec).getEncoded();
        spec.clearPassword();
        if (actual.length != expected.length) return false;
        int diff = 0;
        for (int i = 0; i < actual.length; i++) diff |= actual[i] ^ expected[i];
        return diff == 0;
    }

    private String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) result.append(String.format("%02x", b));
        return result.toString();
    }

    private byte[] fromHex(String value) {
        byte[] data = new byte[value.length() / 2];
        for (int i = 0; i < data.length; i++)
            data[i] = (byte) Integer.parseInt(value.substring(i * 2, i * 2 + 2), 16);
        return data;
    }

    public boolean register(String name, String email, String password) throws Exception {
        String sql = "INSERT INTO users(name,email,password_hash,role) VALUES(?,?,?,'STUDENT')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashPassword(password));
            return ps.executeUpdate() == 1;
        }
    }

    public User login(String email, String password) throws Exception {
        String sql = "SELECT user_id,name,email,password_hash,role FROM users WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && verifyPassword(password, rs.getString("password_hash"))) {
                    return new User(rs.getInt("user_id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("role"));
                }
            }
        }
        return null;
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT user_id,name,email,role FROM users WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new User(rs.getInt("user_id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("role"));
            }
        }
        return null;
    }

    public java.util.List<User> findFaculty() throws SQLException {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT user_id,name,email,role FROM users WHERE role='FACULTY' ORDER BY name";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) users.add(new User(rs.getInt("user_id"), rs.getString("name"),
                    rs.getString("email"), rs.getString("role")));
        }
        return users;
    }
}
