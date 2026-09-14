package org.example.oop2_finalproject.Database_Service;

import org.example.oop2_finalproject.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsRepository {

    private final DatabaseService db;

    public ProductsRepository() {
        db = new DatabaseService();
    }

    // ==============================================================
    // ADD PRODUCT
    // ==============================================================
    public boolean addProduct(String name, int categoryId, double price, int stock, String barcode) {
        String query = "INSERT INTO products (name, category_id, price, stock, barcode) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setInt(2, categoryId);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setString(5, barcode);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add product error: " + e.getMessage());
        }

        return false;
    }

    // ==============================================================
    // UPDATE PRODUCT
    // ==============================================================
    public boolean updateProduct(int id, String newName, int newCategoryId,
                                 double newPrice, int newStock, String newBarcode) {

        String query = "UPDATE products SET name = ?, category_id = ?, price = ?, stock = ?, barcode = ? WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, newName);
            ps.setInt(2, newCategoryId);
            ps.setDouble(3, newPrice);
            ps.setInt(4, newStock);
            ps.setString(5, newBarcode);
            ps.setInt(6, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update product error: " + e.getMessage());
        }

        return false;
    }

    // ==============================================================
    // DELETE PRODUCT
    // ==============================================================
    public boolean deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete product error: " + e.getMessage());
        }

        return false;
    }

    // ==============================================================
    // GET ALL PRODUCTS
    // ==============================================================
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products ORDER BY id ASC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("category_id"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("barcode")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Get all products error: " + e.getMessage());
        }

        return products;
    }

    // ==============================================================
    // SEARCH PRODUCTS (BY NAME OR BARCODE)
    // ==============================================================
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE name LIKE ? OR barcode LIKE ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("category_id"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("barcode")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search products error: " + e.getMessage());
        }

        return products;
    }

    // ==============================================================
    // GET PRODUCTS BY CATEGORY
    // ==============================================================
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category_id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("category_id"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("barcode")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Get products by category error: " + e.getMessage());
        }

        return products;
    }

    // ==============================================================
    // UPDATE STOCK (Used during checkout)
    // ==============================================================
    public boolean updateStock(int productId, int newStock) {
        String query = "UPDATE products SET stock = ? WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, newStock);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update stock error: " + e.getMessage());
        }

        return false;
    }
}
