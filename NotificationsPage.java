package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NotificationsPage {
    private final SceneManager sceneManager;

    public NotificationsPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Notifications");
        Label notification1 = new Label("New job match found: Software Engineer at TechCorp");
        Label notification2 = new Label("Your application for Software Developer is under review.");

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            sceneManager.switchTo("Dashboard");
        });

        layout.getChildren().addAll(titleLabel, notification1, notification2, backButton);
        return new Scene(layout);
    }
}
