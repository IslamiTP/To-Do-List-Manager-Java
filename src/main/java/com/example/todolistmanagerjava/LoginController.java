package com.example.todolistmanagerjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createAccountButton;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Load existing users from file
        users.setAll(UserPersistence.loadUsers());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Authenticate the user
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

    @FXML
    private void handleCreateAccount() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password cannot be empty.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Create new user
        User newUser = new User(username, password);
        users.add(newUser);

        // Save the new user to file
        try {
            UserPersistence.saveUsers(users);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while saving the user.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully. You can now log in.", ButtonType.OK);
        alert.showAndWait();
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
