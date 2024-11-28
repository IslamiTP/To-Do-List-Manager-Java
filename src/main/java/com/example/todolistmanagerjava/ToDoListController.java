package com.example.todolistmanagerjava;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;

public class ToDoListController {

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, Integer> idColumn;
    @FXML
    private TableColumn<Task, String> titleColumn;
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

    private final ObservableList<Task> taskList = FXCollections.observableArrayList();
    private User currentUser;
    private int taskIdCounter = 1;

    public void setUser(User user) {
        this.currentUser = user;
        taskList.setAll(user.getTaskList());

        taskIdCounter = taskList.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        recurrenceColumn.setCellValueFactory(new PropertyValueFactory<>("recurrence"));

        taskTable.setItems(taskList);

        addTaskButton.setOnAction(event -> addTask());
        deleteTaskButton.setOnAction(event -> deleteTask());
        editTaskButton.setOnAction(event -> editTask());
        saveTaskButton.setOnAction(event -> saveTasks());
    }

    private void addTask() {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");

        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Not started", "In-progress", "Completed"));
        statusChoiceBox.setValue("Not started");

        ChoiceBox<String> priorityChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        priorityChoiceBox.setValue("Medium");

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
                String recurrence = recurrenceChoiceBox.getValue().equals("None") ? null : recurrenceChoiceBox.getValue();
                return new Task(taskIdCounter++, titleField.getText(), statusChoiceBox.getValue(),
                        descriptionField.getText(), dueDatePicker.getValue(),
                        priorityChoiceBox.getValue(), recurrence);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(task -> {
            taskList.add(task);
            currentUser.getTaskList().add(task);
        });
    }

    private void deleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
            currentUser.getTaskList().remove(selectedTask);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a task to delete.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void editTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a task to edit.", ButtonType.OK);
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
            com.example.todolistmanagerjava.TaskPersistence.saveTasks(taskList);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Tasks have been successfully saved.", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while saving tasks. " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
