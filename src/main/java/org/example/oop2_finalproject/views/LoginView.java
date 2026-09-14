package org.example.oop2_finalproject.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.oop2_finalproject.controllers.LoginController;

public class LoginView {

    private final LoginController controller;

    public LoginView(Stage stage) {
        controller = new LoginController(stage, this);
        buildUI(stage);
    }

    public void buildUI(Stage stage) {

        // Title
        Label title = new Label("Ralph & Ali's POS\n        Welcome");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Username input
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Password input
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Login Button
        Button loginBtn = new Button("Login");

        loginBtn.setOnAction(e ->
                controller.handleLogin(usernameField.getText(), passwordField.getText())
        );

        // Layout
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(title, usernameField, passwordField, loginBtn);

        Scene scene = new Scene(root, 350, 300);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
