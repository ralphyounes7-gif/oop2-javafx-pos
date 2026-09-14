package org.example.oop2_finalproject.Database_Service;

import org.example.oop2_finalproject.models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesRepository {

    private final DatabaseService db;

    public CategoriesRepository() {
        db = new DatabaseService();
    }

    // ======================================================
    // INSERT CATEGORY
    // ======================================================
    public boolean addCategory(String name) {
        String query = "INSERT INTO categories (name) VALUES (?)";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add category error: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // UPDATE CATEGORY
    // ======================================================
    public boolean updateCategory(int id, String newName) {
        String query = "UPDATE categories SET name = ? WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, newName);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update category error: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // DELETE CATEGORY
    // ======================================================
    public boolean deleteCategory(int id) {
        String query = "DELETE FROM categories WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete category error: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // GET ALL CATEGORIES
    // ======================================================
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories ORDER BY id ASC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Get categories error: " + e.getMessage());
        }

        return categories;
    }

    // ======================================================
    // SEARCH BY NAME (Partial Match)
    // ======================================================
    public List<Category> searchCategories(String keyword) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories WHERE name LIKE ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search category error: " + e.getMessage());
        }

        return categories;
    }
}
