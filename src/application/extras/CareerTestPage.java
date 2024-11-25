package application.extras;

import application.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

public class CareerTestPage {
    private final SceneManager sceneManager;
    private final BorderPane dashboardRoot;

    public CareerTestPage(SceneManager sceneManager, BorderPane dashboardRoot) {
        this.sceneManager = sceneManager;
        this.dashboardRoot = dashboardRoot;
    }

    public VBox getView() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        // Question Label
        Label questionLabel = new Label("What is your preferred work environment?");
        questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Options
        Button optionA = new Button("Option A: Office");
        Button optionB = new Button("Option B: Remote");

        // Back Button
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            Label welcomeContent = new Label("Welcome to Job Connect Dashboard!");
            welcomeContent.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            VBox welcomeView = new VBox(welcomeContent);
            welcomeView.setAlignment(Pos.CENTER);
            dashboardRoot.setCenter(welcomeView);
        });

        layout.getChildren().addAll(questionLabel, optionA, optionB, backButton);
        return layout;
    }
}
