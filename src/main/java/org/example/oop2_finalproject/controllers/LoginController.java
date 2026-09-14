package org.example.oop2_finalproject.controllers;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.oop2_finalproject.Database_Service.EmployeesRepository;
import org.example.oop2_finalproject.models.Admin;
import org.example.oop2_finalproject.models.Cashier;
import org.example.oop2_finalproject.views.AdminDashboardView;
import org.example.oop2_finalproject.views.CashierDashboardView;

public class LoginController {

    private final Stage stage;
    private final EmployeesRepository repo;

    public LoginController(Stage stage, Object view) {
        this.stage = stage;
        this.repo = new EmployeesRepository();
    }

    // =====================================================
    // HANDLE LOGIN
    // =====================================================
    public void handleLogin(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Missing Fields", "Please enter both username and password.");
            return;
        }

        // Try admin login
        Admin admin = repo.loginAdmin(username, password);
        if (admin != null) {
            openAdminDashboard(admin);
            return;
        }

        // Try cashier login
        Cashier cashier = repo.loginCashier(username, password);
        if (cashier != null) {
            openCashierDashboard(cashier);
            return;
        }

        // Invalid login
        showAlert("Login Failed", "Invalid username or password.");
    }

    // =====================================================
    // OPEN ADMIN DASHBOARD
    // =====================================================
    private void openAdminDashboard(Admin admin) {
        new AdminDashboardView(stage, admin);
    }

    // =====================================================
    // OPEN CASHIER DASHBOARD
    // =====================================================
    private void openCashierDashboard(Cashier cashier) {
        new CashierDashboardView(stage, cashier);
    }

    // =====================================================
    // HELPER: ALERT BOX
    // =====================================================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
