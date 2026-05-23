package com.example.library_management.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class AuthWindowFactory {
    private static final String BASE = "/com/example/library_management/";

    public static Scene createLoginScene() throws IOException {
        return new Scene(new FXMLLoader(AuthWindowFactory.class.getResource(BASE + "login-view.fxml")).load());
    }

    public static Scene createSignUpScene() throws IOException {
        return new Scene(new FXMLLoader(AuthWindowFactory.class.getResource(BASE + "signup-view.fxml")).load());
    }
}
