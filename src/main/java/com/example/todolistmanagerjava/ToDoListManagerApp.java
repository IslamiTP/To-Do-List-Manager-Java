package com.example.todolistmanagerjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ToDoListManagerApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Load the LoginView.fxml as the initial screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/todolistmanagerjava/LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            stage.setTitle("Task Manager - Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
