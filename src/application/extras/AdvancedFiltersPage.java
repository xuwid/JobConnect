package application.extras;

import application.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AdvancedFiltersPage {
    private final SceneManager sceneManager;

    public AdvancedFiltersPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        // Title
        Label filtersTitle = new Label("Advanced Filters");

        // Input fields
        TextField salaryRange = new TextField();
        salaryRange.setPromptText("Enter Salary Range");
        
        TextField jobType = new TextField();
        jobType.setPromptText("Enter Job Type (e.g., Full-Time)");
        
        TextField location = new TextField();
        location.setPromptText("Enter Location");

        // Apply Filters Button
        Button applyFiltersButton = new Button("Apply Filters");
        applyFiltersButton.setOnAction(e -> {
            // Handle filter logic here (e.g., pass filter criteria to Job Listings)
            System.out.println("Filters applied: Salary = " + salaryRange.getText() +
                               ", Job Type = " + jobType.getText() +
                               ", Location = " + location.getText());
        });

        // Back Button
        Button backButton = new Button("Back to Job Listings");
        backButton.setOnAction(e -> {
            sceneManager.switchTo("JobListings");
        });

        layout.getChildren().addAll(filtersTitle, salaryRange, jobType, location, applyFiltersButton, backButton);

        return new Scene(layout, 900, 600);
    }
}
