package com.example.todolistmanagerjava;

import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;

public class TaskPersistence {

    public static void saveTasks(ObservableList<Task> taskList, String username) throws IOException {
        String filePath = "tasks_" + username + ".txt"; // File name based on username
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : taskList) {
                writer.write(task.getId() + "|" +
                        task.getTitle() + "|" +
                        task.getStatus() + "|" +
                        task.getDescription() + "|" +
                        (task.getDueDate() == null ? "null" : task.getDueDate()) + "|" +
                        task.getPriority() + "|" +
                        (task.getRecurrence() == null ? "None" : task.getRecurrence()));
                writer.newLine();
            }
        }
    }

    public static int loadTasks(ObservableList<Task> taskList, String username) {
        String filePath = "tasks_" + username + ".txt"; // File name based on username
        File file = new File(filePath);
        if (!file.exists()) {
            return 0; // If the file doesn't exist, no tasks to load, start IDs at 1
        }

        int maxId = 0; // Track the highest ID

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String status = parts[2];
                String description = parts[3];
                LocalDate dueDate = "null".equals(parts[4]) ? null : LocalDate.parse(parts[4]);
                String priority = parts[5];
                String recurrence = parts[6].equals("None") ? null : parts[6];

                taskList.add(new Task(id, title, status, description, dueDate, priority, recurrence));
                if (id > maxId) {
                    maxId = id; // Update the highest ID found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxId;
    }

}
