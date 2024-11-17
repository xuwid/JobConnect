package application;
import javafx.application.Application;
import javafx.stage.Stage;

public class JobConnectApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage);

        // Add scenes to the manager
        LoginPage loginPage = new LoginPage(sceneManager);
        sceneManager.addScene("LoginPage", loginPage.getScene());

        Dashboard dashboard = new Dashboard(sceneManager, "Job Seeker");
        sceneManager.addScene("Dashboard", dashboard.getScene());

        Dashboard dashboardPoster = new Dashboard(sceneManager, "Job Poster");
        sceneManager.addScene("DashboardPoster", dashboardPoster.getScene());

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
