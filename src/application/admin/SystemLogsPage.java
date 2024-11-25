package application.admin;

import Backend.persistence.DatabaseHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import application.SceneManager;

public class SystemLogsPage {
    private final SceneManager sceneManager;
    private final BorderPane root;
    private final DatabaseHandler databaseHandler; // Backend handler
    private TextArea logsArea; // Display area for logs

    public SystemLogsPage(SceneManager sceneManager, BorderPane root) {
        this.sceneManager = sceneManager;
        this.root = root;
        this.databaseHandler = DatabaseHandler.getInstance(); // Singleton instance of DatabaseHandler
    }

    /**
     * Returns the interface for viewing system logs.
     */
    public VBox getView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // Title
        Label title = new Label("System Logs");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Search Bar
        HBox searchBar = createSearchBar();

        // Logs Display Area
        logsArea = new TextArea();
        logsArea.setEditable(false);
        logsArea.setPrefHeight(400);
        logsArea.setPrefWidth(600);
        logsArea.setStyle("-fx-font-family: monospace;");

        // Populate logs initially from the backend
        updateLogsArea(databaseHandler.getAllLogs());

        // Button to Download Logs
        Button downloadButton = new Button("Download Logs");
        downloadButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        downloadButton.setOnAction(e -> downloadLogs());

        layout.getChildren().addAll(title, searchBar, logsArea, downloadButton);
        return layout;
    }

    /**
     * Creates a search bar with search and filter options.
     */
    private HBox createSearchBar() {
        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER);
        searchBar.setPadding(new Insets(10));
        searchBar.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;");

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search logs by keywords");
        searchField.setPrefWidth(300);

        // Filter by Date
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Filter by Date");

        // Search Button
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");
        searchButton.setOnAction(e -> performSearch(searchField.getText(), datePicker.getValue()));

        searchBar.getChildren().addAll(searchField, datePicker, searchButton);
        return searchBar;
    }

    /**
     * Updates the logs display area.
     */
    private void updateLogsArea(List<String> filteredLogs) {
        logsArea.clear();
        if (filteredLogs.isEmpty()) {
            logsArea.setText("No logs found for the specified criteria.");
        } else {
            logsArea.setText(String.join("\n", filteredLogs));
        }
    }

    /**
     * Filters logs based on a keyword and/or date.
     */
    private void performSearch(String keyword, LocalDate date) {
        // Fetch logs from backend
        List<String> logs = databaseHandler.getAllLogs();

        // Filter logs based on the keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Further filter logs by date
        if (date != null) {
            String dateString = date.toString(); // Format the date to match log entries
            logs = logs.stream()
                    .filter(log -> log.contains(dateString))
                    .collect(Collectors.toList());
        }

        updateLogsArea(logs);
    }

    /**
     * Allows the user to download the logs as a text file.
     */
    private void downloadLogs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Logs");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(logsArea.getText());
                System.out.println("Logs saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error saving logs: " + e.getMessage());
            }
        }
    }
}
