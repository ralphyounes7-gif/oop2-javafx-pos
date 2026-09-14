package org.example.oop2_finalproject.Database_Service;

import org.example.oop2_finalproject.models.Cashier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CashiersRepository {

    private final DatabaseService db;

    public CashiersRepository() {
        db = new DatabaseService();
    }

    // ============================================
    // INSERT NEW CASHIER
    // ============================================
    public boolean addCashier(String username, String password) {
        String query = "INSERT INTO cashiers (username, password) VALUES (?, ?)";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add cashier error: " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // UPDATE CASHIER
    // ============================================
    public boolean updateCashier(int id, String newUsername, String newPassword) {
        String query = "UPDATE cashiers SET username = ?, password = ? WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, newUsername);
            ps.setString(2, newPassword);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update cashier error: " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // DELETE CASHIER
    // ============================================
    public boolean deleteCashier(int id) {
        String query = "DELETE FROM cashiers WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete cashier error: " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // GET ALL CASHIERS
    // ============================================
    public List<Cashier> getAllCashiers() {
        List<Cashier> cashiers = new ArrayList<>();
        String query = "SELECT * FROM cashiers ORDER BY id ASC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cashiers.add(new Cashier(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Get cashiers error: " + e.getMessage());
        }

        return cashiers;
    }

    // ============================================
    // SEARCH CASHIER BY USERNAME (partial match)
    // ============================================
    public List<Cashier> searchCashiers(String keyword) {
        List<Cashier> cashiers = new ArrayList<>();
        String query = "SELECT * FROM cashiers WHERE username LIKE ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cashiers.add(new Cashier(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search cashier error: " + e.getMessage());
        }

        return cashiers;
    }
}
