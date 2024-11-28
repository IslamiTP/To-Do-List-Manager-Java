package com.example.todolistmanagerjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        users.add(new User("admin", "password")); // Add default test user
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<User> matchingUser = users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();

        if (matchingUser.isPresent()) {
            loadTaskManager(matchingUser.get());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void loadTaskManager(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todolistmanagerjava/ToDoListView.fxml"));
            Scene scene = new Scene(loader.load());
            ToDoListController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Task Manager - " + user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
