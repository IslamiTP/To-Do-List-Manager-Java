module com.example.todolistmanagerjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.todolistmanagerjava to javafx.fxml;
    exports com.example.todolistmanagerjava;
}