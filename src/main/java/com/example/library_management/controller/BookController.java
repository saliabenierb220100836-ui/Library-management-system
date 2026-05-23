package com.example.library_management.controller;

import com.example.library_management.model.Book;
import com.example.library_management.model.Genre;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.GenreRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.sql.SQLException;

public class BookController {
    private static final int PAGE_SIZE = 10;

    @FXML private TextField idField;
    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private ComboBox<Genre> genreComboBox;
    @FXML private TextField publishedYearField;
    @FXML private TextField totalCopiesField;
    @FXML private TextField availableCopiesField;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> genreColumn;
    @FXML private TableColumn<Book, Integer> publishedYearColumn;
    @FXML private TableColumn<Book, Integer> totalCopiesColumn;
    @FXML private TableColumn<Book, Integer> availableCopiesColumn;
    @FXML private Pagination bookPagination;

    private final BookRepository bookRepository = new BookRepository();
    private final GenreRepository genreRepository = new GenreRepository();
    private final ObservableList<Book> books = FXCollections.observableArrayList();
    private final ObservableList<Genre> genres = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        publishedYearColumn.setCellValueFactory(new PropertyValueFactory<>("publishedYear"));
        totalCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));

        bookTable.setItems(books);
        genreComboBox.setItems(genres);

        bookTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, selected) -> populateForm(selected));

        bookPagination.setPageFactory(this::createPage);
        bookPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            try {
                loadPage(newIndex.intValue());
            } catch (SQLException e) {
                showError("Database error", e.getMessage());
            }
        });

        loadGenres();
        refreshPagination();
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        if (!validateForm(true)) return;
        try {
            bookRepository.insert(buildBook(false));
            clearForm();
            refreshPagination();
        } catch (Exception e) {
            showError("Unable to add book", e.getMessage());
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (!validateForm(false)) return;
        try {
            bookRepository.update(buildBook(true));
            clearForm();
            refreshPagination();
        } catch (Exception e) {
            showError("Unable to update book", e.getMessage());
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Book selected = bookTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No selection", "Choose a book to delete.");
            return;
        }
        try {
            bookRepository.delete(selected.getId());
            clearForm();
            refreshPagination();
        } catch (Exception e) {
            showError("Unable to delete book", e.getMessage());
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearForm();
    }

    private void refreshPagination() {
        try {
            int total = bookRepository.countAll();
            int pageCount = Math.max(1, (int) Math.ceil(total / (double) PAGE_SIZE));
            bookPagination.setPageCount(pageCount);
            int pageIndex = Math.min(bookPagination.getCurrentPageIndex(), pageCount - 1);
            bookPagination.setCurrentPageIndex(pageIndex);
            loadPage(pageIndex);
        } catch (SQLException e) {
            showError("Database error", e.getMessage());
        }
    }

    private javafx.scene.Node createPage(int pageIndex) {
        return new Region();
    }

    private void loadPage(int pageIndex) throws SQLException {
        int offset = pageIndex * PAGE_SIZE;
        books.setAll(bookRepository.findPage(PAGE_SIZE, offset));
        bookTable.getSelectionModel().clearSelection();
    }

    private Book buildBook(boolean includeId) {
        Book book = new Book();
        if (includeId) {
            book.setId(Integer.parseInt(idField.getText().trim()));
        }
        book.setIsbn(isbnField.getText().trim());
        book.setTitle(titleField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setGenre(genreComboBox.getValue().getName());
        book.setPublishedYear(Integer.parseInt(publishedYearField.getText().trim()));
        book.setTotalCopies(Integer.parseInt(totalCopiesField.getText().trim()));
        book.setAvailableCopies(Integer.parseInt(availableCopiesField.getText().trim()));
        return book;
    }

    private boolean validateForm(boolean allowEmptyId) {
        if (!allowEmptyId && idField.getText().trim().isEmpty()) {
            showError("No selection", "Select a book from the table first.");
            return false;
        }
        if (isbnField.getText().trim().isEmpty()
                || titleField.getText().trim().isEmpty()
                || authorField.getText().trim().isEmpty()
                || genreComboBox.getValue() == null
                || publishedYearField.getText().trim().isEmpty()
                || totalCopiesField.getText().trim().isEmpty()
                || availableCopiesField.getText().trim().isEmpty()) {
            showError("Missing data", "All fields are required.");
            return false;
        }
        try {
            int year = Integer.parseInt(publishedYearField.getText().trim());
            int total = Integer.parseInt(totalCopiesField.getText().trim());
            int available = Integer.parseInt(availableCopiesField.getText().trim());
            if (year < 1 || total < 0 || available < 0 || available > total) {
                showError("Invalid values", "Year must be positive; available copies cannot exceed total.");
                return false;
            }
            if (!allowEmptyId) {
                Integer.parseInt(idField.getText().trim());
            }
        } catch (NumberFormatException e) {
            showError("Invalid number", "Year, total copies, and available copies must be numeric.");
            return false;
        }
        return true;
    }

    private void populateForm(Book book) {
        if (book == null) return;
        idField.setText(String.valueOf(book.getId()));
        isbnField.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        genreComboBox.getItems().stream()
                .filter(g -> g.getName().equals(book.getGenre()))
                .findFirst()
                .ifPresent(genreComboBox::setValue);
        publishedYearField.setText(String.valueOf(book.getPublishedYear()));
        totalCopiesField.setText(String.valueOf(book.getTotalCopies()));
        availableCopiesField.setText(String.valueOf(book.getAvailableCopies()));
    }

    private void clearForm() {
        idField.clear();
        isbnField.clear();
        titleField.clear();
        authorField.clear();
        genreComboBox.setValue(null);
        publishedYearField.clear();
        totalCopiesField.clear();
        availableCopiesField.clear();
        bookTable.getSelectionModel().clearSelection();
    }

    private void loadGenres() {
        try {
            genres.setAll(genreRepository.findAll());
        } catch (SQLException e) {
            showError("Database error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
