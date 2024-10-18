package com.example.todolistmanagerjava;

import javafx.collections.ObservableList;
import java.io.*;
import java.time.LocalDate;

public class TaskPersistence {

    private static final String FILE_PATH = "tasks.txt"; // Change this to an absolute path if needed for testing

    public static void saveTasks(ObservableList<Task> taskList) throws IOException {
        System.out.println("Saving tasks to: " + FILE_PATH); // Debug statement

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Task task : taskList) {
                writer.write(task.getId() + "|" +
                        task.getTitle() + "|" +
                        task.getStatus() + "|" +
                        task.getDescription() + "|" +
                        task.getDueDate() + "|" +
                        task.getPriority());
                writer.newLine();

                // Print each task as it's saved
                System.out.println("Saved task: " + task);
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
            throw e; // Re-throw to handle in the controller
        }
    }

    public static void loadTasks(ObservableList<Task> taskList) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String status = parts[2];
                String description = parts[3];
                LocalDate dueDate = LocalDate.parse(parts[4]);
                String priority = parts[5];
                taskList.add(new Task(id, title, status, description, dueDate, priority));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
