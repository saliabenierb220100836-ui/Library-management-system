package com.example.library_management.app;

import com.example.library_management.factory.AuthWindowFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LibraryApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = AuthWindowFactory.createLoginScene();
        stage.setTitle("Login – Library Management System");
        stage.setScene(scene);
        stage.show();
    }
}
