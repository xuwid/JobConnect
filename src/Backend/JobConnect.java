package Backend;

import Backend.controllers.*;
import Backend.entities.*;
import Backend.models.User;

import java.util.List;
import java.util.Optional;

/**
 * JobConnect serves as the central controller for communication between the frontend and backend.
 */
public class JobConnect {
    private static JobConnect instance;

    private final UserController userController;
    private final JobController jobController;
    private final ApplicationController applicationController;
    private final NotificationController notificationController;
    private final SupportQueryController supportQueryController;
    private final SystemLogsController logsController;

    // Private constructor (singleton pattern)
    private JobConnect() {
        this.userController = new UserController();
        this.jobController = new JobController();
        this.applicationController = new ApplicationController();
        this.notificationController = new NotificationController();
        this.supportQueryController = new SupportQueryController();
        this.logsController = new SystemLogsController();
    }

    // Singleton pattern to ensure a single instance
    public static synchronized JobConnect getInstance() {
        if (instance == null) {
            instance = new JobConnect();
        }
        return instance;
    }

    // ---------- USER MANAGEMENT ---------- //
    public Optional<User> authenticateUser(String email, String password) {
        return userController.authenticateUser(email, password);
    }

    public boolean registerUser(String name, String email, String password, String role) {
        return userController.register(name, email, password, role);
    }
    
    public boolean registerUser(User user) {
        return userController.register(user);
    }

    public List<User> getAllUsers() {
        return userController.getAllUsers();
    }

    public boolean deleteUser(int userId) {
        return userController.deleteUser(userId);
    }

    public boolean updateUser(User user) {
        return userController.updateUser(user);
    }
    public Optional<User> getUserById(int userId) {
		return userController.getUserById(userId);
	}
	public void updateSession(User currentUser) {
		// TODO Auto-generated method stub
		userController.updateSession(currentUser);
	}

	public User getSessionUser() {
		// TODO Auto-generated method stub
		return userController.getCurrentUser();
	}

	public void logoutUser() {
		userController.logoutUser();
	}
    // ---------- JOB MANAGEMENT ---------- //
    public List<Job> getAllJobs() {
        return jobController.getAllJobs();
    }

    public Optional<Job> getJobById(int jobId) {
        return jobController.getJobById(jobId);
    }

    public boolean addJob(Job job) {
        return jobController.addJob(job);
    }

    public boolean updateJob(Job job) {
        return jobController.updateJob(job);
    }

    public boolean deleteJob(int jobId) {
        return jobController.deleteJob(jobId);
    }
    
    
    

    // ---------- APPLICATION MANAGEMENT ---------- //
    public boolean applyForJob(Application application) {
        return applicationController.applyForJob(application);
    }

    public boolean updateApplicationStatus(int applicationId, String status) {
        return applicationController.updateApplicationStatus(applicationId, status);
    }

    public List<Application> getApplicationsForUser(int userId) {
        return applicationController.getApplicationsForUser(userId);
    }

    public List<Application> getApplicationsForJob(int jobId) {
        return applicationController.getApplicationsForJob(jobId);
    }

    
    

    // ---------- NOTIFICATION MANAGEMENT ---------- //
    public List<Notification> getNotificationsForUser(int userId) {
        return notificationController.getNotificationsForUser(userId);
    }

    public boolean markNotificationAsRead(int notificationId) {
        return notificationController.markNotificationAsRead(notificationId);
    }

    public boolean addNotification(Notification notification) {
        return notificationController.addNotification(notification);
    }

    public boolean deleteNotification(int notificationId) {
        return notificationController.deleteNotification(notificationId);
    }

    public boolean deleteAllNotificationsForUser(int userId) {
        return notificationController.deleteAllNotificationsForUser(userId);
    }
	public boolean notify(int userId, String message) {
	   return notificationController.notify(userId, message);
	}


    // ---------- SUPPORT QUERY MANAGEMENT ---------- //
    public boolean addSupportQuery(SupportQuery query) {
        return supportQueryController.addSupportQuery(query);
    }

    public List<SupportQuery> getAllSupportQueries() {
        return supportQueryController.getAllSupportQueries();
    }

    public boolean resolveSupportQuery(int queryId) {
        return supportQueryController.resolveSupportQuery(queryId);
    }

    public List<SupportQuery> getSupportQueriesByUserId(int userId) {
        return supportQueryController.getSupportQueriesByUserId(userId);
    }
    public Optional<SupportQuery> getSupportQueryById(int queryId) {
        return supportQueryController.getSupportQueryById(queryId);
    }

 // ----------------- Log Management -----------------

    public void logEvent(String message, int userId) {
        logsController.logMessage(message, userId);
    }
    
    public List<String> getSystemLogs(int userId) {
        logsController.logMessage("Fetching all system logs.", userId);
        return logsController.getAllLogs();
    }

    public boolean clearSystemLogs(int userId) {
        logsController.logMessage("Clearing all system logs.", userId);
        return logsController.deleteAllLogs();
    }

    public List<String> searchSystemLogs(String keyword, int userId) {
        logsController.logMessage("Searching logs with keyword: '" + keyword + "'", userId);
        return logsController.searchLogs(keyword);
    }





}
