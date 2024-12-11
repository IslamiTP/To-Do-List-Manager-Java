package com.example.todolistmanagerjava;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.PriorityQueue;

import java.time.LocalDate;

public class ToDoListController {

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, Integer> idColumn;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, LocalDate> dueDateColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> recurrenceColumn;
    @FXML
    private Button addTaskButton;
    @FXML
    private Button deleteTaskButton;
    @FXML
    private Button editTaskButton;
    @FXML
    private Button saveTaskButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button logoutButton;
    @FXML
    private Button sortPriorityButton;
    @FXML
    private Button filterPriorityButton;
    @FXML
    private Button sortIdButton;




    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private FilteredList<Task> filteredTaskList;

    private PriorityQueue<Integer> unusedIds = new PriorityQueue<>();
    private int taskIdCounter = 1;

    private User currentUser;


    public void setUser(User user) {
        this.currentUser = user;
        this.taskList = user.getTaskList();
        this.filteredTaskList = new FilteredList<>(taskList, p -> true);
        taskTable.setItems(filteredTaskList);

        // Load tasks and update the task ID pool
        int maxId = TaskPersistence.loadTasks(taskList, currentUser.getUsername());
        taskIdCounter = maxId + 1;
        unusedIds.clear();

        // Populate the pool with IDs that are not in use
        for (int i = 1; i < taskIdCounter; i++) {
            int id = i;
            if (taskList.stream().noneMatch(task -> task.getId() == id)) {
                unusedIds.add(id);
            }
        }
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        recurrenceColumn.setCellValueFactory(new PropertyValueFactory<>("recurrence"));

        filteredTaskList = new FilteredList<>(taskList, p -> true);
        taskTable.setItems(filteredTaskList);

        setupSearchFeature();
        setupButtonActions();
    }

    private void setupButtonActions() {
        addTaskButton.setOnAction(event -> addTask());
        deleteTaskButton.setOnAction(event -> deleteTask());
        editTaskButton.setOnAction(event -> editTask());
        saveTaskButton.setOnAction(event -> saveTasks());
        logoutButton.setOnAction(event -> handleLogout());
    }

    private void setupSearchFeature() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTaskList.setPredicate(task -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return task.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                        task.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void addTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");

        // Set up dialog fields
        TextField titleField = new TextField();
        titleField.setPromptText("Title (max 100 characters)");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description (max 2000 characters)");

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Not started", "In-progress", "Completed"));
        ChoiceBox<String> priorityChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        ChoiceBox<String> recurrenceChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("None", "Daily", "Weekly", "Monthly"));
        recurrenceChoiceBox.setValue("None");

        GridPane grid = new GridPane();
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusChoiceBox, 1, 3);
        grid.add(new Label("Priority:"), 0, 4);
        grid.add(priorityChoiceBox, 1, 4);
        grid.add(new Label("Recurrence:"), 0, 5);
        grid.add(recurrenceChoiceBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String title = titleField.getText();
                String description = descriptionField.getText();

                if (title.length() > 100 || description.length() > 2000) {
                    return null; // Validation logic can go here
                }

                int taskId = unusedIds.isEmpty() ? taskIdCounter++ : unusedIds.poll();
                String recurrence = recurrenceChoiceBox.getValue().equals("None") ? null : recurrenceChoiceBox.getValue();
                return new Task(taskId,
                        title,
                        statusChoiceBox.getValue(),
                        description,
                        dueDatePicker.getValue(),
                        priorityChoiceBox.getValue(),
                        recurrence);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(task -> taskList.add(task));
    }


    private void deleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
            unusedIds.add(selectedTask.getId()); // Add the ID back to the pool
        }
    }

    private void editTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            return; // No task selected
        }

        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Edit Task");

        TextField titleField = new TextField(selectedTask.getTitle());
        TextField descriptionField = new TextField(selectedTask.getDescription());
        DatePicker dueDatePicker = new DatePicker(selectedTask.getDueDate());

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Not started", "In-progress", "Completed"));
        statusChoiceBox.setValue(selectedTask.getStatus());

        ChoiceBox<String> priorityChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityChoiceBox.setValue(selectedTask.getPriority());

        ChoiceBox<String> recurrenceChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("None", "Daily", "Weekly", "Monthly"));
        recurrenceChoiceBox.setValue(selectedTask.getRecurrence() == null ? "None" : selectedTask.getRecurrence());

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;"); // Style for error messages

        GridPane grid = new GridPane();
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusChoiceBox, 1, 3);
        grid.add(new Label("Priority:"), 0, 4);
        grid.add(priorityChoiceBox, 1, 4);
        grid.add(new Label("Recurrence:"), 0, 5);
        grid.add(recurrenceChoiceBox, 1, 5);
        grid.add(errorLabel, 1, 6); // Add error label below the fields

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String title = titleField.getText();
                String description = descriptionField.getText();

                // Validate input lengths
                if (title.length() > 100) {
                    errorLabel.setText("Title cannot exceed 100 characters.");
                    return null;
                }
                if (description.length() > 2000) {
                    errorLabel.setText("Description cannot exceed 2000 characters.");
                    return null;
                }

                errorLabel.setText(""); // Clear any previous error messages
                selectedTask.setTitle(title);
                selectedTask.setDescription(description);
                selectedTask.setDueDate(dueDatePicker.getValue());
                selectedTask.setStatus(statusChoiceBox.getValue());
                selectedTask.setPriority(priorityChoiceBox.getValue());
                selectedTask.setRecurrence(recurrenceChoiceBox.getValue().equals("None") ? null : recurrenceChoiceBox.getValue());
                return selectedTask;
            }
            return null;
        });

        dialog.showAndWait();
        taskTable.refresh();
    }


    private void saveTasks() {
        try {
            TaskPersistence.saveTasks(taskList, currentUser.getUsername());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Tasks have been successfully saved.", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while saving tasks. " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/todolistmanagerjava/LoginView.fxml"));
            Scene loginScene = new Scene(loader.load());

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Task Manager - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Track the current sort order for IDs
    private boolean isIdAscending = true;

    @FXML
    private void handleSortById() {
        // Sort the task list by ID
        taskList.sort((task1, task2) -> {
            if (isIdAscending) {
                return Integer.compare(task1.getId(), task2.getId());
            } else {
                return Integer.compare(task2.getId(), task1.getId());
            }
        });

        // Toggle the sort order for next click
        isIdAscending = !isIdAscending;

        // Update the button text to reflect the next action
        sortIdButton.setText(isIdAscending ? "Sort by ID (Asc)" : "Sort by ID (Desc)");

        // Refresh the table to reflect the sorted data
        taskTable.refresh();
    }

    private boolean isAscending = true;

    @FXML
    private void handleSortByPriority() {
        // Sort the task list by priority
        taskList.sort((task1, task2) -> {
            // Define the priority order: High > Medium > Low
            String priority1 = task1.getPriority();
            String priority2 = task2.getPriority();

            int comparison;
            if (priority1.equals(priority2)) {
                comparison = 0;
            } else if (priority1.equals("High")) {
                comparison = -1;
            } else if (priority1.equals("Medium")) {
                comparison = priority2.equals("High") ? 1 : -1;
            } else { // Low
                comparison = 1;
            }

            // Reverse the comparison result if sorting in descending order
            return isAscending ? comparison : -comparison;
        });

        // Toggle the sort order for next click
        isAscending = !isAscending;

        // Update the button text to reflect the next action
        sortPriorityButton.setText(isAscending ? "Sort by Priority (Asc)" : "Sort by Priority (Desc)");

        // Refresh the table to reflect the sorted data
        taskTable.refresh();
    }

    private String currentFilter = "All";

    @FXML
    private void handleFilterByPriority() {
        // Cycle through the filter options
        switch (currentFilter) {
            case "All":
                currentFilter = "High";
                filterPriorityButton.setText("Filter: High");
                break;
            case "High":
                currentFilter = "Medium";
                filterPriorityButton.setText("Filter: Medium");
                break;
            case "Medium":
                currentFilter = "Low";
                filterPriorityButton.setText("Filter: Low");
                break;
            case "Low":
                currentFilter = "All";
                filterPriorityButton.setText("Filter: All");
                break;
        }

        // Apply the filter to the task list
        applyPriorityFilter();
    }

    private void applyPriorityFilter() {
        filteredTaskList.setPredicate(task -> {
            if (currentFilter.equals("All")) {
                return true; // Show all tasks
            }
            return task.getPriority().equals(currentFilter);
        });
    }
}
