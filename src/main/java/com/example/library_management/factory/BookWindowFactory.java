package com.example.library_management.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class BookWindowFactory {
    private static final String BASE = "/com/example/library_management/";

    public static Scene createScene() throws IOException {
        return new Scene(new FXMLLoader(BookWindowFactory.class.getResource(BASE + "book-view.fxml")).load());
    }
}
