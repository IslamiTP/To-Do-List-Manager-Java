package com.example.todolistmanagerjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    private final String username;
    private final String password; // In a real app, passwords should be hashed
    private final ObservableList<Task> taskList;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.taskList = FXCollections.observableArrayList();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ObservableList<Task> getTaskList() {
        return taskList;
    }

    public String serialize() {
        // Serialize user details to a single line: username|password
        return username + "|" + password;
    }

    public static User deserialize(String line) {
        // Deserialize a single line into a User object
        String[] parts = line.split("\\|");
        return new User(parts[0], parts[1]);
    }
}
