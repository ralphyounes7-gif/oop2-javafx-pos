package org.example.oop2_finalproject.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.oop2_finalproject.models.Cashier;
import org.example.oop2_finalproject.views.cashier.CartView;
import org.example.oop2_finalproject.views.cashier.CheckoutView;
import org.example.oop2_finalproject.views.cashier.ProductSearchView;

public class CashierDashboardView {

    private final Stage stage;
    private final Cashier cashier;

    private BorderPane mainLayout;
    private VBox menu;

    public CashierDashboardView(Stage stage, Cashier cashier) {
        this.stage = stage;
        this.cashier = cashier;
        buildUI();
    }


    private void buildUI() {

        mainLayout = new BorderPane();

        menu = buildMenu();

        mainLayout.setCenter(buildWelcomeScreen());

        mainLayout.setLeft(menu);

        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Cashier Dashboard");
        stage.show();
    }


    private VBox buildMenu() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setPrefWidth(220);
        box.setStyle("-fx-background-color: #2C3E50;");

        Text title = new Text("CASHIER DASHBOARD");
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Text welcome = new Text("Logged in as: " + cashier.getUsername());
        welcome.setFill(Color.LIGHTGRAY);

        Button searchProductsBtn = buildMenuButton("Search Products");
        Button cartBtn = buildMenuButton("View Cart");
        Button checkoutBtn = buildMenuButton("Checkout");
        Button logoutBtn = buildMenuButton("Logout");



        searchProductsBtn.setOnAction(e -> mainLayout.setCenter(new ProductSearchView()));
        cartBtn.setOnAction(e -> mainLayout.setCenter(new CartView()));
        checkoutBtn.setOnAction(e -> mainLayout.setCenter(new CheckoutView()));
        logoutBtn.setOnAction(e -> logout());

        box.getChildren().addAll(
                title,
                welcome,
                searchProductsBtn,
                cartBtn,
                checkoutBtn,
                logoutBtn
        );

        return box;
    }

    private Button buildMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setStyle(
                "-fx-background-color: #34495E;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 5;"
        );
        return btn;
    }


    private VBox buildWelcomeScreen() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        Text title = new Text("Welcome, " + cashier.getUsername());
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Text subtitle = new Text("Select an action from the menu");
        subtitle.setStyle("-fx-font-size: 18px;");

        box.getChildren().addAll(title, subtitle);

        return box;
    }


    private void logout() {
        new LoginView(stage);
    }
}
