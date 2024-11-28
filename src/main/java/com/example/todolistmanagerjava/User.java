package com.example.todolistmanagerjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    private final String username;
    private final String password; // In production, passwords should be hashed
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
}
