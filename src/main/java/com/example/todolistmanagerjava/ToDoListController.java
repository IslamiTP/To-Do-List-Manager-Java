package com.example.todolistmanagerjava;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    private final ObservableList<Task> taskList = FXCollections.observableArrayList();
    private int taskIdCounter = 1; // To automatically assign IDs to tasks

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
        //implement addTSask
    }

    private void deleteTask() {
        //implement deleteTask
    }

    private void editTask() {
        //Implement edit task
    }

    private void saveTasks() {
        //implement save feature
    }
}
