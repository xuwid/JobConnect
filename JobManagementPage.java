package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class JobManagementPage {
    private final SceneManager sceneManager;

    public JobManagementPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Manage Your Jobs");
        
        Button editJobButton = new Button("Edit Job Listings");
		editJobButton.setOnAction(e -> {
			sceneManager.switchTo("EditJobListings");
		});
		
		Button DeleteJobButton = new Button("Delete Job Listings");
		DeleteJobButton.setOnAction(e -> {
			sceneManager.switchTo("DeleteJobListings");
        });
		
        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(e -> {
            sceneManager.switchTo("DashboardPoster");
        });
             

        layout.getChildren().addAll(titleLabel, editJobButton, DeleteJobButton,backButton);
        return new Scene(layout);
    }
}
