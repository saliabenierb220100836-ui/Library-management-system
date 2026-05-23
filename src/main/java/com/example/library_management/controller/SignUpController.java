package com.example.library_management.controller;

import com.example.library_management.factory.AuthWindowFactory;
import com.example.library_management.repository.AuthRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignUpController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private final AuthRepository authRepository = new AuthRepository();

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Missing data", "All fields are required.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Password mismatch", "Passwords do not match.");
            return;
        }
        try {
            if (authRepository.usernameExists(username)) {
                showError("Username taken", "That username is already in use.");
                return;
            }
            authRepository.register(username, password);
            showInfo("Account created", "You can now log in.");
            goToLogin();
        } catch (Exception e) {
            showError("Sign-up error", e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) throws Exception {
        goToLogin();
    }

    private void goToLogin() throws RuntimeException {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(AuthWindowFactory.createLoginScene());
            stage.setTitle("Login – Library Management System");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
