package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegisterPage {
    private final SceneManager sceneManager;

    public RegisterPage(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Enter Password");

        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Job Seeker", "Job Poster");
        roleComboBox.setPromptText("Select Role");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getValue();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
                System.out.println("Please fill all fields and select a role.");
            } else {
                System.out.println("User Registered as " + role + "!");
                Dashboard dashboard = new Dashboard(sceneManager, role);
                sceneManager.addScene("Dashboard", dashboard.getScene());
                sceneManager.switchTo("Dashboard");
            }
        });

        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> {
            sceneManager.switchTo("LoginPage");
        });

        layout.getChildren().addAll(usernameField, emailField, passwordField, roleComboBox, registerButton, backButton);
        return new Scene(layout, 400, 400);
    }
}
