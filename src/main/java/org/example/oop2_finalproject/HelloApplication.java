package org.example.oop2_finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.oop2_finalproject.views.LoginView;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        new LoginView(stage);
    }


    public static void main(String[] args) {
        launch();
    }
}