module com.example.library_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.example.library_management.controller to javafx.fxml;
    opens com.example.library_management.model to javafx.base;
    exports com.example.library_management.app;
}
