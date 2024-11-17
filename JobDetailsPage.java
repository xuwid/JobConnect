package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class JobDetailsPage {
    private final SceneManager sceneManager;

    public JobDetailsPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Software Engineer at TechCorp");
        Label descriptionLabel = new Label("Job Description: Develop and maintain software applications.");

        Button applyButton = new Button("Apply Now");
        Button backButton = new Button("Back to Job Listings");

        backButton.setOnAction(e -> {
            sceneManager.switchTo("JobListings");
        });

        layout.getChildren().addAll(titleLabel, descriptionLabel, applyButton, backButton);
        return new Scene(layout);
    }
}
