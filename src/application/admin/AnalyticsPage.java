package application.admin;
import application.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AnalyticsPage {
    private final SceneManager sceneManager;

    public AnalyticsPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Job Analytics");
        Label metric1 = new Label("Total Views: 500");
        Label metric2 = new Label("Applications Received: 25");
        Label metric3 = new Label("Average Conversion Rate: 5%");

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            sceneManager.switchTo("DashboardPoster");
        });

        layout.getChildren().addAll(titleLabel, metric1, metric2, metric3, backButton);
        return new Scene(layout);
    }
}
