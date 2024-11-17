package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class JobListingsPage {
    private final SceneManager sceneManager;

    public JobListingsPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        // Title of the page
        Label titleLabel = new Label("Available Jobs");

        // Button to navigate to Job Details
        Button jobDetailsButton = new Button("View Job Details");
        jobDetailsButton.setOnAction(e -> {
            // Create and add JobDetailsPage to the SceneManager
            if (!sceneManager.hasScene("JobDetails")) {
                JobDetailsPage jobDetailsPage = new JobDetailsPage(sceneManager);
                sceneManager.addScene("JobDetails", jobDetailsPage.getScene());
            }
            sceneManager.switchTo("JobDetails");
        });

        // Back button to go to the Dashboard
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            sceneManager.switchTo("Dashboard");
        });

        layout.getChildren().addAll(titleLabel, jobDetailsButton, backButton);

        return new Scene(layout);
    }
}
