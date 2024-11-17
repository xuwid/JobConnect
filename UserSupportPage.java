package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class UserSupportPage {
    private final SceneManager sceneManager;

    public UserSupportPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("User Support");
        TextArea queryArea = new TextArea();
        queryArea.setPromptText("Enter your query...");

        Button submitQueryButton = new Button("Submit Query");
        Button backButton = new Button("Back to Dashboard");

        backButton.setOnAction(e -> {
            sceneManager.switchTo("Dashboard");
        });

        layout.getChildren().addAll(titleLabel, queryArea, submitQueryButton, backButton);
        return new Scene(layout);
    }
}
