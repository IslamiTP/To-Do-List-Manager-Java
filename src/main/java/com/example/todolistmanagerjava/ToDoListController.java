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

    private ObservableList<Task> taskList = FXCollections.observableArrayList();
    private FilteredList<Task> filteredTaskList;
    private int taskIdCounter = 1;
    private User currentUser;

    public void setUser(User user) {
        this.currentUser = user;
        this.taskList = user.getTaskList();
        this.filteredTaskList = new FilteredList<>(taskList, p -> true);

        // Initialize the TableView with the user's tasks
        taskTable.setItems(filteredTaskList);

        // Load tasks from the user's file
        TaskPersistence.loadTasks(taskList, currentUser.getUsername());
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
        titleField.setPromptText("Title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Not started", "In-progress", "Completed"));
        ChoiceBox<String> priorityChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        ChoiceBox<String> recurrenceChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("None", "Daily", "Weekly", "Monthly"));
        recurrenceChoiceBox.setValue("None");

        // Layout dialog
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
                String recurrence = recurrenceChoiceBox.getValue().equals("None") ? null : recurrenceChoiceBox.getValue();
                return new Task(taskIdCounter++,
                        titleField.getText(),
                        statusChoiceBox.getValue(),
                        descriptionField.getText(),
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
        }
    }

    private void editTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Select a Task to Edit.", ButtonType.OK);
            alert.showAndWait();
            return;
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
                selectedTask.setTitle(titleField.getText());
                selectedTask.setDescription(descriptionField.getText());
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
}
