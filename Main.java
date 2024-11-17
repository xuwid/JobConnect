package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager sceneManager = new SceneManager(primaryStage);

            // Add scenes
            sceneManager.addScene("LoginPage", new LoginPage(sceneManager).getScene());
            sceneManager.addScene("RegisterPage", new RegisterPage(sceneManager).getScene());

            // Set initial scene
            sceneManager.switchTo("LoginPage");

            primaryStage.setTitle("Application Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
