package org.example.oop2_finalproject.Database_Service;

import org.example.oop2_finalproject.models.SaleItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleItemsRepository {

    private final DatabaseService db;

    public SaleItemsRepository() {
        db = new DatabaseService();
    }

    // ==============================================================
    // ADD SALE ITEM
    // ==============================================================
    public boolean addSaleItem(int saleId, int productId, int quantity, double price, double total) {

        String query = """
                INSERT INTO sale_items (sale_id, product_id, quantity, price, total)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, saleId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.setDouble(5, total);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add sale item error: " + e.getMessage());
        }

        return false;
    }

    // ==============================================================
    // GET ALL ITEMS FOR A SALE
    // ==============================================================
    public List<SaleItem> getSaleItems(int saleId) {
        List<SaleItem> items = new ArrayList<>();
        String query = "SELECT * FROM sale_items WHERE sale_id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, saleId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(new SaleItem(
                        rs.getInt("id"),
                        rs.getInt("sale_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("total")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Get sale items error: " + e.getMessage());
        }

        return items;
    }

    // ==============================================================
    // DELETE ALL ITEMS FROM A SALE (if sale is canceled)
    // ==============================================================
    public boolean deleteSaleItems(int saleId) {
        String query = "DELETE FROM sale_items WHERE sale_id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, saleId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete sale items error: " + e.getMessage());
        }

        return false;
    }

    // ==============================================================
    // DELETE SINGLE SALE ITEM
    // ==============================================================
    public boolean deleteSaleItem(int id) {
        String query = "DELETE FROM sale_items WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete sale item error: " + e.getMessage());
        }

        return false;
    }

    // ==============================================================
    // GET TOTAL AMOUNT OF A SALE (SUM OF ITEMS)
    // ==============================================================
    public double getSaleItemsTotal(int saleId) {
        String query = "SELECT SUM(total) AS sale_total FROM sale_items WHERE sale_id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, saleId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("sale_total");
            }

        } catch (SQLException e) {
            System.out.println("Get sale items total error: " + e.getMessage());
        }

        return 0.0;
    }
}
