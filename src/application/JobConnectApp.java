package application;

import Backend.persistence.SessionManager;
import application.user.LoginPage;
import javafx.application.Application;
import javafx.stage.Stage;

public class JobConnectApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);

        // Add Login Page
        LoginPage loginPage = new LoginPage(sceneManager);
        sceneManager.addScene("LoginPage", loginPage.getScene());

        primaryStage.setTitle("Job Connect Application");
        sceneManager.switchTo("LoginPage");
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
