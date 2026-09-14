package org.example.oop2_finalproject.Database_Service;

import org.example.oop2_finalproject.models.Admin;
import org.example.oop2_finalproject.models.Cashier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeesRepository {

    private final DatabaseService db;

    public EmployeesRepository() {
        db = new DatabaseService();
    }


    public Admin loginAdmin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (Exception e) {
            System.out.println("Admin login error: " + e.getMessage());
        }

        return null; // invalid login
    }


    public Cashier loginCashier(String username, String password) {
        String query = "SELECT * FROM cashiers WHERE username = ? AND password = ?";

        try {
            Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Cashier(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (Exception e) {
            System.out.println("Cashier login error: " + e.getMessage());
        }

        return null;
    }
}