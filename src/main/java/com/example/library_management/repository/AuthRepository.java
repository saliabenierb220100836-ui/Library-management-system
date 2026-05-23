package com.example.library_management.repository;

import com.example.library_management.util.Database;
import com.example.library_management.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthRepository {

    public boolean authenticate(String username, String password) throws SQLException {
        String hashed = PasswordUtil.hash(password);
        String sql = "SELECT id FROM user_accounts WHERE username = ? AND password_hash = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, hashed);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT id FROM user_accounts WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void register(String username, String password) throws SQLException {
        String sql = "INSERT INTO user_accounts (username, password_hash) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, PasswordUtil.hash(password));
            statement.executeUpdate();
        }
    }
}
