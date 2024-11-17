package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginPage {
    private final SceneManager sceneManager;

    public LoginPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            // Placeholder: Replace with actual user authentication logic
            String userType = "Job Seeker"; // Assume "Job Seeker" for demo purposes
            Dashboard dashboard = new Dashboard(sceneManager, userType);
            sceneManager.addScene("Dashboard", dashboard.getScene());
            sceneManager.switchTo("Dashboard");
        });

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            RegisterPage registerPage = new RegisterPage(sceneManager);
            sceneManager.addScene("Register", registerPage.getScene());
            sceneManager.switchTo("Register");
        });

        layout.getChildren().addAll(usernameField, passwordField, loginButton, registerButton);
        return new Scene(layout);
    }
}
