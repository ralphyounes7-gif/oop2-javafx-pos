package org.example.oop2_finalproject.Database_Service;
import org.example.oop2_finalproject.models.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminsRepository {

    private final DatabaseService db;

    public AdminsRepository() {
        db = new DatabaseService();
    }

    // ===================================================
    // INSERT ADMIN
    // ===================================================
    public boolean addAdmin(String username, String password) {
        String query = "INSERT INTO admins (username, password) VALUES (?, ?)";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add admin error: " + e.getMessage());
            return false;
        }
    }

    // ===================================================
    // UPDATE ADMIN
    // ===================================================
    public boolean updateAdmin(int id, String newUsername, String newPassword) {
        String query = "UPDATE admins SET username = ?, password = ? WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, newUsername);
            ps.setString(2, newPassword);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update admin error: " + e.getMessage());
            return false;
        }
    }

    // ===================================================
    // DELETE ADMIN
    // ===================================================
    public boolean deleteAdmin(int id) {
        String query = "DELETE FROM admins WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete admin error: " + e.getMessage());
            return false;
        }
    }

    // ===================================================
    // GET ALL ADMINS
    // ===================================================
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM admins ORDER BY id ASC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Get all admins error: " + e.getMessage());
        }

        return admins;
    }

    // ===================================================
    // SEARCH ADMIN BY USERNAME (partial match)
    // ===================================================
    public List<Admin> searchAdmins(String keyword) {
        List<Admin> admins = new ArrayList<>();
        String query = "SELECT * FROM admins WHERE username LIKE ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search admin error: " + e.getMessage());
        }

        return admins;
    }
}