package Backend.controllers;

import Backend.persistence.DatabaseHandler;

import java.util.List;

/**
 * Controller class for managing system logs.
 * It interacts with the DatabaseHandler to handle system log operations.
 */
public class SystemLogsController {
    private final DatabaseHandler databaseHandler;

    /**
     * Constructor initializes the database handler.
     */
    public SystemLogsController() {
        this.databaseHandler = DatabaseHandler.getInstance(); // Singleton instance of DatabaseHandler
    }

    /**
     * Logs a new system message associated with a specific user.
     *
     * @param logMessage The message to log.
     * @param userId     The ID of the user associated with this log.
     * @return true if the log was saved successfully, false otherwise.
     */
    public boolean logMessage(String logMessage, int userId) {
        return databaseHandler.saveLog(logMessage, userId);
    }

    /**
     * Retrieves all system logs.
     *
     * @return A list of all system logs.
     */
    public List<String> getAllLogs() {
        return databaseHandler.getAllLogs();
    }

    /**
     * Retrieves logs containing a specific keyword or phrase.
     *
     * @param keyword The keyword to search for in the logs.
     * @return A list of logs containing the keyword.
     */
    public List<String> searchLogs(String keyword) {
        return databaseHandler.searchLogs(keyword);
    }

    /**
     * Retrieves logs associated with a specific user.
     *
     * @param userId The ID of the user whose logs are to be retrieved.
     * @return A list of logs associated with the given user.
     */
    public List<String> getLogsByUserId(int userId) {
        return databaseHandler.getLogsByUserID(userId);
    }

    /**
     * Deletes all system logs.
     *
     * @return true if logs were deleted successfully, false otherwise.
     */
    public boolean deleteAllLogs() {
        return databaseHandler.deleteAllLogs();
    }

    /**
     * Deletes logs containing a specific keyword or phrase.
     *
     * @param keyword The keyword for logs to delete.
     * @return true if logs were deleted successfully, false otherwise.
     */
    public boolean deleteLogsByKeyword(String keyword) {
        return databaseHandler.deleteLogsByKeyword(keyword);
    }

    /**
     * Adds an initialization log indicating system startup.
     */
    public void logSystemStartup() {
        logMessage("System started successfully.", 0); // System logs with UserID = 0 (admin/system)
    }

    /**
     * Logs a user login event.
     *
     * @param userId   The ID of the logged-in user.
     * @param userName The username of the logged-in user.
     */
    public void logUserLogin(int userId, String userName) {
        logMessage("User '" + userName + "' logged in.", userId);
    }

    /**
     * Logs a user logout event.
     *
     * @param userId   The ID of the logged-out user.
     * @param userName The username of the logged-out user.
     */
    public void logUserLogout(int userId, String userName) {
        logMessage("User '" + userName + "' logged out.", userId);
    }

    /**
     * Logs an error or exception event.
     *
     * @param userId       The ID of the user associated with the error (use 0 for system errors).
     * @param errorDetails The details of the error or exception.
     */
    public void logError(int userId, String errorDetails) {
        logMessage("Error: " + errorDetails, userId);
    }
}
