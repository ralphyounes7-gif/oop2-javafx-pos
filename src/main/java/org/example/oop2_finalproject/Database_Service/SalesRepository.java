package org.example.oop2_finalproject.Database_Service;

import org.example.oop2_finalproject.models.Sale;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesRepository {

    private final DatabaseService db;

    public SalesRepository() {
        db = new DatabaseService();
    }

    // ==============================================================
    // CREATE SALE (INSERT HEADER)
    // ==============================================================
    public int createSale(int cashierId, Integer customerId,
                          double subtotal, double tax, double discount, double total) {

        String query = """
                INSERT INTO sales (cashier_id, customer_id, date, subtotal, tax, discount, total)
                VALUES (?, ?, NOW(), ?, ?, ?, ?)
                """;

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, cashierId);

            if (customerId == null)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, customerId);

            ps.setDouble(3, subtotal);
            ps.setDouble(4, tax);
            ps.setDouble(5, discount);
            ps.setDouble(6, total);

            ps.executeUpdate();

            // get the generated sale id
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // sale_id
            }

        } catch (SQLException e) {
            System.out.println("Create sale error: " + e.getMessage());
        }

        return -1; // sale creation failed
    }

    // ==============================================================
    // GET ALL SALES
    // ==============================================================
    public List<Sale> getAllSales() {
        List<Sale> list = new ArrayList<>();
        String query = "SELECT * FROM sales ORDER BY date DESC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapSale(rs));
            }

        } catch (SQLException e) {
            System.out.println("Get all sales error: " + e.getMessage());
        }

        return list;
    }

    // ==============================================================
    // GET SALES BY CASHIER
    // ==============================================================
    public List<Sale> getSalesByCashier(int cashierId) {
        List<Sale> list = new ArrayList<>();
        String query = "SELECT * FROM sales WHERE cashier_id = ? ORDER BY date DESC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, cashierId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapSale(rs));
            }

        } catch (SQLException e) {
            System.out.println("Get sales by cashier error: " + e.getMessage());
        }

        return list;
    }

    // ==============================================================
    // GET SALES BY DATE (yyyy-mm-dd)
    // ==============================================================
    public List<Sale> getSalesByDate(String date) {
        List<Sale> list = new ArrayList<>();
        String query = "SELECT * FROM sales WHERE DATE(date) = ? ORDER BY date DESC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, date);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapSale(rs));
            }

        } catch (SQLException e) {
            System.out.println("Get sales by date error: " + e.getMessage());
        }

        return list;
    }

    // ==============================================================
    // SEARCH SALES (BY ID)
    // ==============================================================
    public Sale getSaleById(int id) {
        String query = "SELECT * FROM sales WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapSale(rs);
            }

        } catch (SQLException e) {
            System.out.println("Search sale error: " + e.getMessage());
        }

        return null;
    }

    // ==============================================================
    // MAP RESULTSET TO SALE OBJECT
    // ==============================================================
    private Sale mapSale(ResultSet rs) throws SQLException {
        return new Sale(
                rs.getInt("id"),
                rs.getInt("cashier_id"),
                rs.getObject("customer_id") == null ? null : rs.getInt("customer_id"),
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getDouble("subtotal"),
                rs.getDouble("tax"),
                rs.getDouble("discount"),
                rs.getDouble("total")
        );
    }
}
