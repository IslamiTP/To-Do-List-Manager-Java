package com.example.todolistmanagerjava;

import javafx.collections.ObservableList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TaskPersistence {

    private static final String FILE_PATH = "tasks.txt";

    public static void saveTasks(ObservableList<Task> taskList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task task : taskList) {
                writer.write(task.getId() + "|" +
                        task.getTitle() + "|" +
                        task.getStatus() + "|" +
                        task.getDescription() + "|" +
                        task.getDueDate() + "|" +
                        task.getPriority() + "|" +
                        (task.getRecurrence() != null ? task.getRecurrence() : "None"));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
