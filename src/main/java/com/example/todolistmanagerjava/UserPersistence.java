package com.example.todolistmanagerjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class UserPersistence {

    private static final String FILE_PATH = "users.txt";

    public static void saveUsers(ObservableList<User> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.write(user.serialize());
                writer.newLine();
            }
        }
    }

    public static ObservableList<User> loadUsers() {
        ObservableList<User> users = FXCollections.observableArrayList();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return users; // Return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(User.deserialize(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}
