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
        // Create a simple dialog for task entry
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

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Task(taskIdCounter++,
                        titleField.getText(),
                        statusChoiceBox.getValue(),
                        descriptionField.getText(),
                        dueDatePicker.getValue(),
                        priorityChoiceBox.getValue());
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
        //Implement edit task
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            //If task not selected error message will be displayed
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please Select a Task to Edit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        //Dialog for task editing
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Edit Task");

        // Prefills the dialog box field with the previous text in order to change it.
        TextField titleField = new TextField(selectedTask.getTitle());
        TextField descriptionField = new TextField(selectedTask.getDescription());
        DatePicker dueDatePicker = new DatePicker(selectedTask.getDueDate());

        ChoiceBox<String> statuesChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Not Started", "In progress", "Completed"));
        statuesChoiceBox.setValue(selectedTask.getStatus());

        ChoiceBox<String> priorityChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Low", "Medium", "High"));
        statuesChoiceBox.setValue(selectedTask.getPriority());

        //Grid Layout of dialog fields
        GridPane grid = new GridPane();
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statuesChoiceBox, 1, 3);
        grid.add(new Label("Priority:"), 0, 4);
        grid.add(priorityChoiceBox, 1, 4);
        // Dialog field buttons
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        //Update task if user clicks on "OK"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                selectedTask.setTitle(titleField.getText());
                selectedTask.setDescription(descriptionField.getText());
                selectedTask.setDueDate(dueDatePicker.getValue());
                selectedTask.setStatus(statuesChoiceBox.getValue());
                selectedTask.setPriority(priorityChoiceBox.getValue());
                return selectedTask;
            }
            return null;
        });

        // Show dialog message and refresh table if tasks updated
        dialog.showAndWait();
        taskTable.refresh();
    }

    private void saveTasks() {
        //implement save feature
    }
}
