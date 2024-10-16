package com.example.todolistmanagerjava;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Button addTaskButton;
    @FXML
    private Button deleteTaskButton;
    @FXML
    private Button editTaskButton;
    @FXML
    private Button saveTaskButton;
    @FXML
    private TextField searchField;

    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        taskTable.setItems(taskList);
        setupButtonActions();
    }

    private void setupButtonActions() {
        addTaskButton.setOnAction(event -> addTask());
        deleteTaskButton.setOnAction(event -> deleteTask());
        editTaskButton.setOnAction(event -> editTask());
        saveTaskButton.setOnAction(event -> saveTasks());
    }

    private void addTask() {
        // Implementation for adding a task
    }

    private void deleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
        }
    }

    private void editTask() {
        // Implementation for editing a task
    }

    private void saveTasks() {
        // Implementation for saving tasks
    }
}
