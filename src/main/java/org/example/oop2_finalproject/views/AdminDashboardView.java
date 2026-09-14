package org.example.oop2_finalproject.views;

import org.example.oop2_finalproject.views.admin.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.oop2_finalproject.models.Admin;

public class AdminDashboardView {

    private final Stage stage;
    private final Admin admin;

    private BorderPane mainLayout;
    private VBox menu;

    public AdminDashboardView(Stage stage, Admin admin) {
        this.stage = stage;
        this.admin = admin;

        buildUI();
    }


    private void buildUI() {

        mainLayout = new BorderPane();

        menu = buildMenu();

        mainLayout.setCenter(buildWelcomeScreen());

        mainLayout.setLeft(menu);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }


    private VBox buildMenu() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setPrefWidth(220);
        box.setStyle("-fx-background-color: #2C3E50;");

        Text title = new Text("ADMIN DASHBOARD");
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text welcome = new Text("Logged in as: " + admin.getUsername());
        welcome.setFill(Color.LIGHTGRAY);

        Button manageAdmins = buildMenuButton("Manage Admins");
        Button manageCashiers = buildMenuButton("Manage Cashiers");
        Button manageCategories = buildMenuButton("Manage Categories");
        Button manageProducts = buildMenuButton("Manage Products");
        Button manageCustomers = buildMenuButton("Manage Customers");
        Button viewSales = buildMenuButton("View Sales Report");
        Button logoutBtn = buildMenuButton("Logout");

        manageAdmins.setOnAction(e -> mainLayout.setCenter(new ManageAdminsView()));
        manageCashiers.setOnAction(e -> mainLayout.setCenter(new ManageCashiersView()));
        manageCategories.setOnAction(e ->
                mainLayout.setCenter(new ManageCategoriesView())
        );
        manageProducts.setOnAction(e->
                mainLayout.setCenter(new ManageProductsView())
        );
        manageCustomers.setOnAction(e ->
                mainLayout.setCenter(new ManageCustomersView())
        );
        viewSales.setOnAction(e->
                mainLayout.setCenter(new SalesReportView().getRoot())
        );
        logoutBtn.setOnAction(e -> logout());

        box.getChildren().addAll(
                title, welcome,
                manageAdmins,
                manageCashiers,
                manageCategories,
                manageProducts,
                manageCustomers,
                viewSales,
                logoutBtn
        );

        return box;
    }

    private Button buildMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setStyle(
                "-fx-background-color: #34495E; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-background-radius: 5;"
        );
        return btn;
    }


    private VBox buildWelcomeScreen() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        Text title = new Text("Welcome, " + admin.getUsername());
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Text subtitle = new Text("Select a section from the menu");
        subtitle.setStyle("-fx-font-size: 18px;");

        box.getChildren().addAll(title, subtitle);

        return box;
    }

    private void logout() {
        new LoginView(stage);
    }
}


