package org.example.oop2_finalproject.Database_Service;

import org.example.oop2_finalproject.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomersRepository {

    private final DatabaseService db;

    public CustomersRepository() {
        db = new DatabaseService();
    }

    // ======================================================
    // ADD CUSTOMER
    // ======================================================
    public boolean addCustomer(String name, String phone, int points) {
        String query = "INSERT INTO customers (name, phone, points) VALUES (?, ?, ?)";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setInt(3, points);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add customer error: " + e.getMessage());
        }

        return false;
    }

    // ======================================================
    // UPDATE CUSTOMER
    // ======================================================
    public boolean updateCustomer(int id, String newName, String newPhone, int newPoints) {
        String query = "UPDATE customers SET name = ?, phone = ?, points = ? WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, newName);
            ps.setString(2, newPhone);
            ps.setInt(3, newPoints);
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update customer error: " + e.getMessage());
        }

        return false;
    }

    // ======================================================
    // DELETE CUSTOMER
    // ======================================================
    public boolean deleteCustomer(int id) {
        String query = "DELETE FROM customers WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete customer error: " + e.getMessage());
        }

        return false;
    }

    // ======================================================
    // GET ALL CUSTOMERS
    // ======================================================
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers ORDER BY id ASC";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getInt("points")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Get customers error: " + e.getMessage());
        }

        return customers;
    }

    // ======================================================
    // SEARCH CUSTOMERS (BY NAME OR PHONE)
    // ======================================================
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            String like = "%" + keyword + "%";

            ps.setString(1, like);
            ps.setString(2, like);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getInt("points")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Search customers error: " + e.getMessage());
        }

        return customers;
    }

    // ======================================================
    // UPDATE POINTS (Used after a sale)
    // ======================================================
    public boolean updateCustomerPoints(int customerId, int newPoints) {
        String query = "UPDATE customers SET points = ? WHERE id = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, newPoints);
            ps.setInt(2, customerId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update points error: " + e.getMessage());
        }

        return false;
    }
}
