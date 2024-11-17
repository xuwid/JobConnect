package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ProfilePage {
    private final SceneManager sceneManager;

    public ProfilePage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Your Profile");
        TextField nameField = new TextField("Enter your name");
        TextField emailField = new TextField("Enter your email");

        Button saveButton = new Button("Save");
        Button backButton = new Button("Back to Dashboard");

        backButton.setOnAction(e -> {
            sceneManager.switchTo("Dashboard");
        });

        layout.getChildren().addAll(titleLabel, nameField, emailField, saveButton, backButton);
        return new Scene(layout);
    }
}
