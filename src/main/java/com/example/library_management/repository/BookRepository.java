package com.example.library_management.repository;

import com.example.library_management.model.Book;
import com.example.library_management.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM books";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public List<Book> findPage(int limit, int offset) throws SQLException {
        String sql = "SELECT id, isbn, title, author, genre, published_year, total_copies, available_copies " +
                "FROM books ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Book> books = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }
        }
        return books;
    }

    public List<Book> findAll() throws SQLException {
        String sql = "SELECT id, isbn, title, author, genre, published_year, total_copies, available_copies " +
                "FROM books ORDER BY id DESC";
        List<Book> books = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    public void insert(Book book) throws SQLException {
        String sql = "INSERT INTO books (isbn, title, author, genre, published_year, total_copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindBook(statement, book, false);
            statement.executeUpdate();
        }
    }

    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, genre = ?, published_year = ?, " +
                "total_copies = ?, available_copies = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindBook(statement, book, true);
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM books WHERE id = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private void bindBook(PreparedStatement statement, Book book, boolean includeId) throws SQLException {
        statement.setString(1, book.getIsbn());
        statement.setString(2, book.getTitle());
        statement.setString(3, book.getAuthor());
        statement.setString(4, book.getGenre());
        statement.setInt(5, book.getPublishedYear());
        statement.setInt(6, book.getTotalCopies());
        statement.setInt(7, book.getAvailableCopies());
        if (includeId) {
            statement.setInt(8, book.getId());
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("isbn"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getInt("published_year"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies")
        );
    }
}
