package com.example.library_management.repository;

import com.example.library_management.model.Genre;
import com.example.library_management.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreRepository {

    public List<Genre> findAll() throws SQLException {
        String sql = "SELECT id, name FROM genres ORDER BY name";
        List<Genre> genres = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                genres.add(new Genre(rs.getInt("id"), rs.getString("name")));
            }
        }
        return genres;
    }
}
