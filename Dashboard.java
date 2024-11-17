package application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class Dashboard {
    private final SceneManager sceneManager;
    private final String userType;

    public Dashboard(SceneManager sceneManager, String userType) {
        this.sceneManager = sceneManager;
        this.userType = userType;
    }
       public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);

        if (userType.equals("Job Seeker")) {
            Button takeTest = new Button("Take Career Test");
            takeTest.setOnAction(e -> {
                CareerTestPage careerTestPage = new CareerTestPage(sceneManager);
                sceneManager.addScene("CareerTest", careerTestPage.getScene());
                sceneManager.switchTo("CareerTest");
            });

            Button browseJobs = new Button("Browse Jobs");
            browseJobs.setOnAction(e -> {
                JobListingsPage jobListingsPage = new JobListingsPage(sceneManager);
                sceneManager.addScene("JobListings", jobListingsPage.getScene());
                sceneManager.switchTo("JobListings");
            });

            layout.getChildren().addAll(takeTest, browseJobs);
        } else if (userType.equals("Job Poster")) {
            Button postJob = new Button("Post Job");
            
            Button manageJobs = new Button("Manage Jobs");
            layout.getChildren().addAll(postJob, manageJobs);
            manageJobs.setOnAction(e -> {
            	JobManagementPage JobManagementPage = new JobManagementPage(sceneManager);
            	sceneManager.addScene("PostJob", JobManagementPage.getScene());
            	sceneManager.switchTo("PostJob");
            });
        }
        //make button for Notifications and User Support
        
        Button notificationsButton = new Button("Notifications");
		notificationsButton.setOnAction(e -> {
			NotificationsPage notificationsPage = new NotificationsPage(sceneManager);
			sceneManager.addScene("Notifications", notificationsPage.getScene());
			sceneManager.switchTo("Notifications");
		});
		layout.getChildren().add(notificationsButton);
		
		Button userSupportButton = new Button("User Support");
		userSupportButton.setOnAction(e -> {
			UserSupportPage userSupportPage = new UserSupportPage(sceneManager);
            sceneManager.addScene("UserSupport", userSupportPage.getScene());
            sceneManager.switchTo("UserSupport");
                    });
		        layout.getChildren().add(userSupportButton);
		        
		        
        Button profileButton = new Button("Profile");
        profileButton.setOnAction(e -> {
            ProfilePage profilePage = new ProfilePage(sceneManager);
            sceneManager.addScene("Profile", profilePage.getScene());
            sceneManager.switchTo("Profile");
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            sceneManager.switchTo("LoginPage");
        });

        layout.getChildren().addAll(profileButton, logoutButton);

        return new Scene(layout);
    }
}
