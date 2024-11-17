package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MCQTestPage {
    private final SceneManager sceneManager;

    public MCQTestPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        Label questionLabel = new Label("What is your preferred work environment?");
        Button optionA = new Button("Option A: Office");
        Button optionB = new Button("Option B: Remote");

        Button nextButton = new Button("Next");
        Button backButton = new Button("Back to Dashboard");

        backButton.setOnAction(e -> {
            sceneManager.switchTo("Dashboard");
        });

        layout.getChildren().addAll(questionLabel, optionA, optionB, nextButton, backButton);
        return new Scene(layout);
    }
}
