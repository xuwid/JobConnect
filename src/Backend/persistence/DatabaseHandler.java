
package Backend.persistence;

import Backend.entities.Application;
import Backend.entities.Job;
import Backend.entities.Notification;
import Backend.entities.SupportQuery;
import Backend.models.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * DatabaseHandler simulates a database and provides persistence functionality.
 * It uses in-memory data structures for simulating persistent storage.
 */
public class DatabaseHandler {
	private static DatabaseHandler instance;

    private static final String DB_URL = "jdbc:sqlserver://MOMINS-COMPUTER\\SQLEXPRESS02:1433;databaseName=JobConnectApp;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USERNAME = "momin";
    private static final String DB_PASSWORD = "12";

    private Connection connection;

    private DatabaseHandler() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public static synchronized DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
    /**
     * Returns the singleton instance of the DatabaseHandler.
     */

    /**
     * Pre-populates the databases with sample data.
     */
    

    // ----------- USER MANAGEMENT ------------ //

    /**
     * Retrieves all users.
     */
    public List<User> getAllUsers() {
        String query = "SELECT * FROM Users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        }
        return users;
    }
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("UserId");
        String name = rs.getString("username");
        String email = rs.getString("Email");
        String password = rs.getString("Password");
        String role = rs.getString("Role");

        switch (role.toLowerCase()) {
            case "admin":
                return new Admin(userId, name, email, password);
            case "job seeker":
                return new JobSeeker(userId, name, email, password);
            case "job poster":
                return new JobPoster(userId, name, email, password);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @return true if the user was deleted, false otherwise.
     */
    public boolean deleteUser(int userId) {
    	
		String query = "DELETE FROM Users WHERE UserId = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, userId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error deleting user: " + e.getMessage());
			return false;
		}
    }
    /**
     * Retrieves a user by their ID.
     */
    public Optional<User> getUserById(int userId) {
        String query = "SELECT * FROM Users WHERE UserId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Bind the userId parameter to the query
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Use the helper method to construct the user object from the result set
                    return Optional.of(createUserFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

	/**
	 * Retrieves a user by their email.
	 */
    public Optional<User> getUserByEmail(String email) {
        String query = "SELECT * FROM Users WHERE Email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createUserFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by email: " + e.getMessage());
        }
        return Optional.empty();
    }

	/**
	 * Retrieves a user by their email.
	 */
    public Optional<User> getUserByUsername(String username) {
        String query = "SELECT * FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createUserFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by name: " + e.getMessage());
        }
        return Optional.empty();
    }
    /**
     * Retrieves a user by username and password.
     *
     * @param username The username to search for.
     * @param password The password to search for.
     * @return An Optional containing the User if found, otherwise Optional.empty().
     */
    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createUserFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by username and password: " + e.getMessage());
        }
        return Optional.empty();
    }

	/**
	 * Register a user
	 */
    public boolean registerUser(String name, String email, String password, String role) {
        String query = "INSERT INTO Users (Name, Email, Password, Role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, role);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return false;
    }
    
    // Overloaded method: accepts a User object
    public boolean registerUser(User user) {
        String query = "INSERT INTO Users (username, Email, Password, Role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Finds the maximum user ID for generating new unique IDs.
     */
    public int getMaxUserId() {
		String query = "SELECT MAX(UserId) FROM Users";
		try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error fetching max user ID: " + e.getMessage());
		}
		return 0; // Return 0 if no users are found
    }

    /**
     * Adds a new user to the database.
     */
//    public boolean saveUser(User user) {
//    	        String query = "INSERT INTO Users (UserId,Name, Email, Password, Role) VALUES (?, ?, ?, ?,?)";
//    	                try (PreparedStatement stmt = connection.prepareStatement(query)) {
//    	                	
//							
//    	                	                            stmt.setInt(1, user.getUserId());
//    	                	                            stmt.setString(2, user.getName());
//    	                	                            stmt.setString(3, user.getEmail());
//    	                	                            stmt.setString(4, user.getPassword());
//    	                	                            stmt.setString(5, user.getRole());
//    	                	                            
//    	                	                         
//							return stmt.executeUpdate() > 0;
//						} catch (SQLException e) {
//							System.err.println("Error saving user: " + e.getMessage());
//							return false;
//    	                }
//    	
//    }
    public boolean updateUser(User updatedUser) {
        String query = "UPDATE Users SET username = ?, Email = ?, Password = ?, Role = ? WHERE UserId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, updatedUser.getName());
            stmt.setString(2, updatedUser.getEmail());
            stmt.setString(3, updatedUser.getPassword());
            stmt.setString(4, updatedUser.getRole());
            stmt.setInt(5, updatedUser.getUserId());
            // Log the query execution

            // Execute the query and check if any row is affected
            boolean result = stmt.executeUpdate() > 0;

            // Log the success or failure
            if (result) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with the given UserId.");
            }
            return result;
        } catch (SQLException e) {
        	System.err.println("Error updating user: " + e.getMessage());
        	return false;                       	
        }
    	
    }

    // ----------- JOB MANAGEMENT ------------ //

    /**
     * Retrieves all jobs.
     */
    public List<Job> getAllJobs(int posterid) {
		String query = "SELECT * FROM Jobs WHERE PostedBy = ?";
		List<Job> jobs = new ArrayList<>();
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, posterid);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					jobs.add(createJobFromResultSet(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching all jobs: " + e.getMessage());
		}
		return jobs;
    }
    public List<Job> getAllJobs() {
    
		String query = "SELECT * FROM Jobs";
		List<Job> jobs = new ArrayList<>();
		try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				jobs.add(createJobFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error fetching all jobs: " + e.getMessage());
		}
		return jobs;
    }
    private Job createJobFromResultSet(ResultSet rs) throws SQLException {
        int jobId = rs.getInt("JobId");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String salary = rs.getString("Salary");
        int postedBy = rs.getInt("PostedBy");
        List<String> requirements = List.of(rs.getString("Requirements").split(","));

        return new Job(jobId, title, description, salary, postedBy, requirements);
    }
    
    

    /**
     * Retrieves a job by its ID.
     */
    public Optional<Job> getJobById(int jobId) {
        
    	        String query = "SELECT * FROM Jobs WHERE JobId = ?";
				try (PreparedStatement stmt = connection.prepareStatement(query)) {
					stmt.setInt(1, jobId);
					try (ResultSet rs = stmt.executeQuery()) {
						if (rs.next()) {
							return Optional.of(createJobFromResultSet(rs));
						}
					}
				} catch (SQLException e) {
					System.err.println("Error fetching job by ID: " + e.getMessage());
				}
				return Optional.empty();
    }

    /**
     * Adds a new job to the database.
     */
    public boolean saveJob(Job job) { 	
    	        String query = "INSERT INTO Jobs (Title, Description, Salary, PostedBy, Requirements) VALUES (?, ?, ?, ?, ?)";
				try (PreparedStatement stmt = connection.prepareStatement(query)) {
					stmt.setString(1, job.getTitle());
					stmt.setString(2, job.getDescription());
					stmt.setString(3, job.getSalary());
					stmt.setInt(4, job.getPosterId());
					stmt.setString(5, String.join(",", job.getRequirements()));
					return stmt.executeUpdate() > 0;
				} catch (SQLException e) {
					System.err.println("Error saving job: " + e.getMessage());
					return false;

				}
    }

    /**
     * Updates an existing job.
     */
    public boolean updateJob(Job updatedJob) {

		String query = "UPDATE Jobs SET Title = ?, Description = ?, Salary = ?, PostedBy = ?, Requirements = ? WHERE JobId = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			
			stmt.setString(1, updatedJob.getTitle());
			stmt.setString(2, updatedJob.getDescription());
			stmt.setString(3, updatedJob.getSalary());
			stmt.setInt(4, updatedJob.getPosterId());
			stmt.setString(5, String.join(",", updatedJob.getRequirements()));
			stmt.setInt(6, updatedJob.getJobId());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error updating job: " + e.getMessage());
			return false;
		}
	}

    
    public List<Job> getJobsBySeekerId(int seekerId) {
        // Updated query with robust logic for filtering un-applied jobs
        String query = """
            SELECT j.*
FROM jobs j
LEFT JOIN job_applications ja
ON j.JobId = ja.JobId AND ja.UserId = ?
WHERE ja.ApplicationId IS NULL;
        """;

        List<Job> jobs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, seekerId); // Bind seeker ID to the query
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    jobs.add(createJobFromResultSet(rs)); // Populate the Job object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching jobs by seeker ID: " + e.getMessage());
        }

        return jobs;
    }

	/**
     * Deletes a job by its ID.
     */
    public boolean deleteJob(int jobId) {
    	
        String query = "DELETE FROM Jobs WHERE JobId = ?";	
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                	                    stmt.setInt(1, jobId);
                	                    return stmt.executeUpdate() > 0;
                	                    } catch (SQLException e) {
                	                    	System.err.println("Error deleting job: " + e.getMessage());
                	                    	return false;
                	                    }
                	                    	
                }

    /**
     * Deletes a job by its ID.
     */

    // ----------- APPLICATION MANAGEMENT ------------ //

    /**
     * Retrieves all applications for a given job ID.
     * 
     * 
     * 
     */
    
	public boolean acceptApplication(int applicationId) {
		String query = "UPDATE job_Applications SET Status = 'Accepted' WHERE ApplicationId = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, applicationId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error accepting application: " + e.getMessage());
			return false;
		}
	}
	public boolean rejectApplication(int applicationId) {
		
		String query = "UPDATE job_Applications SET Status = 'Rejected' WHERE ApplicationId = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, applicationId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Error rejecting application: " + e.getMessage());
			return false;
		}
		
	}

	public boolean applyForJob(Application application) {
    	
    	        String query = "INSERT INTO job_Applications (JobId, UserId, Status, ApplicationDate) VALUES (?, ?, ?, ?)";
    	                        try (PreparedStatement stmt = connection.prepareStatement(query)) {
    	                        	
                                    stmt.setInt(1, application.getJobId());
                                    stmt.setInt(2, application.getUserId());
                                    stmt.setString(3, application.getStatus());
                                    stmt.setTimestamp(4, Timestamp.valueOf(application.getApplicationDate()));
                                    return stmt.executeUpdate() > 0;
                                } catch (SQLException e) {
                                    System.err.println("Error applying for job: " + e.getMessage());
                                    return false;
                                }
	}
    public List<Application> getApplicationsForJob(int jobId) {
        String query = "SELECT * FROM job_Applications WHERE JobId = ?";
        List<Application> applications = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(createApplicationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching applications for job: " + e.getMessage());
        }
        return applications;
    }
    
    /**
     * Retrieves all applications for a given user ID.
     *
     * @param userId The ID of the user.
     * @return List of applications for the user.
     */
    public List<Application> getApplicationsForUser(int userId) {
        String query = "SELECT * FROM job_Applications WHERE UserId = ?";
        List<Application> applications = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(createApplicationFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching applications for user: " + e.getMessage());
        }
        return applications;
    }
    
    
    public Application getApplicationById(int applicationId) {
        String query = "SELECT * FROM job_Applications WHERE ApplicationId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, applicationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createApplicationFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching application by ID: " + e.getMessage());
        }
        return null;
    }



    public List<Application> getApplicationsForPoster(int posterId) {
        String query = """
            SELECT a.*
            FROM job_applications a
            JOIN jobs j ON a.JobId = j.JobId
            WHERE j.PostedBy = ?
        """;

        List<Application> applications = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, posterId); // Bind the poster ID to the query
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    applications.add(createApplicationFromResultSet(rs)); // Create an Application object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching applications for poster ID: " + e.getMessage());
        }

        return applications;
    }
    

	public Job getJobByApplicationId(int applicationId) {
        String query = """
            SELECT j.*
            FROM jobs j
            JOIN job_applications a ON j.JobId = a.JobId
            WHERE a.ApplicationId = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, applicationId); // Bind the application ID to the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createJobFromResultSet(rs); // Create a Job object
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching job by application ID: " + e.getMessage());
        }

        return null;
	}
	public int getJobIdByApplicationId(int applicationId) {
	    String query = "SELECT JobId FROM Applications WHERE ApplicationId = ?";
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setInt(1, applicationId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("JobId"); // Return the JobId if found
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error fetching JobId for ApplicationId " + applicationId + ": " + e.getMessage());
	    }
	    return -1; // Return -1 if no JobId is found
	}
 

    /**
     * Adds a new application to the database.
     */
    /**
     * Saves a new application to the database.
     *
     * @param application The application entity to save.
     * @return true if the application is saved successfully, false otherwise.
     */
    public boolean saveApplication(Application application) {
        String query = "INSERT INTO job_Applications (JobId, UserId, Status, ApplicationDate, CoverLetter) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, application.getJobId());
            stmt.setInt(2, application.getUserId());
            stmt.setString(3, application.getStatus());
            stmt.setTimestamp(4, Timestamp.valueOf(application.getApplicationDate()));
            stmt.setString(5, application.getCoverLetter()); // Save the cover letter
            return stmt.executeUpdate() > 0; // Return true if rows are affected
        } catch (SQLException e) {
            System.err.println("Error saving application: " + e.getMessage());
            return false;
        }
    }


    /**
     * Updates the status of an application.
     *
     * @param applicationId The ID of the application to update.
     * @param newStatus The new status to set (e.g., "Approved", "Rejected").
     * @return true if the status was updated successfully, false otherwise.
     */
    /**
     * Updates the status of an application.
     *
     * @param applicationId The ID of the application to update.
     * @param newStatus The new status to set (e.g., "Accepted", "Rejected").
     * @return true if the status was updated successfully, false otherwise.
     */
    public boolean updateApplicationStatus(int applicationId, String newStatus) {
        String query = "UPDATE job_Applications SET Status = ? WHERE ApplicationId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, applicationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating application status: " + e.getMessage());
            return false;
        }
    }
    private Application createApplicationFromResultSet(ResultSet rs) throws SQLException {
        int applicationId = rs.getInt("ApplicationId");
        int jobId = rs.getInt("JobId");
        int userId = rs.getInt("UserId");
        String status = rs.getString("Status");
        LocalDateTime applicationDate = rs.getTimestamp("ApplicationDate").toLocalDateTime();
        String coverLetter = rs.getString("CoverLetter"); // Retrieve the cover letter

        Application application = new Application(jobId, userId, status, coverLetter);
        application.setApplicationId(applicationId);
        application.setApplicationDate(applicationDate);
        return application;
    }

     

    /**
    

    // ----------- NOTIFICATION MANAGEMENT ------------ //

    /**
     * Retrieves all notifications for a specific user.
     */
    public List<Notification> getNotificationsForUser(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM Notifications WHERE userId = ? ORDER BY date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(new Notification(
                        rs.getInt("notificationId"),
                        rs.getInt("userId"),
                        rs.getString("message"),
                        rs.getTimestamp("date"),
                        rs.getBoolean("isRead")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching notifications: " + e.getMessage());
        }
        return notifications;
    }


    /**
     * Adds a new notification to the database.
     */
    public boolean saveNotification(Notification notification) {
        String query = "INSERT INTO Notifications (userId, message) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving notification: " + e.getMessage());
            return false;
        }
    }
    
    //update application status
   //give me sql server qeury
   //update application status
    //give me sql server qeury
   


    /**
     * Marks a notification as read.
     */
    public boolean markNotificationAsRead(int notificationId) {
        String query = "UPDATE Notifications SET isRead = 1 WHERE notificationId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            return false;
        }
    }

    
    /**
     * Deletes a notification by its ID.
     * @param notificationId The ID of the notification to delete.
     * @return true if the notification was deleted, false otherwise.
     */
    public boolean deleteNotification(int notificationId) {
    	
        
        String query = "DELETE FROM Notifications WHERE NotificationId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
                	
        	stmt.setInt(1, notificationId);
        	return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
            	System.err.println("Error deleting notification: " + e.getMessage());
                return false;
                	}
    }
    /**
     * Pre-populates the notifications database with sample data.
     */
    
    /**
     * Generates a unique notification ID.
     * @return A unique integer ID for notifications.
     */
    private int generateNotificationId() {
    	
		String query = "SELECT MAX(NotificationId) FROM Notifications";
		try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (SQLException e) {
			System.err.println("Error fetching max notification ID: " + e.getMessage());
		}
		return 1; // Return 1 if no notifications are found
	}
 

    

 // ----------- LOGS MANAGEMENT ------------ //

    /**
     * Retrieves all system logs.
     *
     * @return List of all logs in the database.
     */
    public List<String> getAllLogs() {
        String query = "SELECT LogID, Message, Timestamp, UserID FROM Logs ORDER BY Timestamp DESC";
        List<String> logs = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int logID = rs.getInt("LogID");
                String message = rs.getString("Message");
                Timestamp timestamp = rs.getTimestamp("Timestamp");
                int userID = rs.getInt("UserID");

                // Format log message with all details
                logs.add(String.format("LogID: %d | UserID: %d | Timestamp: %s | Message: %s",
                        logID, userID, timestamp.toString(), message));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all logs: " + e.getMessage());
        }
        return logs;
    }

    /**
     * Saves a new system log.
     *
     * @param logMessage The log message to save.
     * @param userID The ID of the user associated with the log.
     * @return true if the log was saved successfully, false otherwise.
     */
    public boolean saveLog(String logMessage, int userID) {
        String query = "INSERT INTO Logs (Message, Timestamp, UserID) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, logMessage);
            stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setInt(3, userID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving log: " + e.getMessage());
        }
        return false;
    }

    /**
     * Searches logs containing a specific keyword.
     *
     * @param keyword The keyword to search for in the logs.
     * @return A list of log messages containing the keyword.
     */
    public List<String> searchLogs(String keyword) {
        String query = "SELECT LogID, Message, Timestamp, UserID FROM Logs WHERE Message LIKE ? ORDER BY Timestamp DESC";
        List<String> logs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int logID = rs.getInt("LogID");
                    String message = rs.getString("Message");
                    Timestamp timestamp = rs.getTimestamp("Timestamp");
                    int userID = rs.getInt("UserID");

                    // Format log message with all details
                    logs.add(String.format("LogID: %d | UserID: %d | Timestamp: %s | Message: %s",
                            logID, userID, timestamp.toString(), message));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching logs: " + e.getMessage());
        }
        return logs;
    }

    /**
     * Deletes all logs from the database.
     *
     * @return true if logs were deleted successfully, false otherwise.
     */
    public boolean deleteAllLogs() {
        String query = "DELETE FROM Logs";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting all logs: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes logs containing a specific keyword.
     *
     * @param keyword The keyword for logs to delete.
     * @return true if logs were deleted successfully, false otherwise.
     */
    public boolean deleteLogsByKeyword(String keyword) {
        String query = "DELETE FROM Logs WHERE Message LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting logs by keyword: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves all logs associated with a specific user.
     *
     * @param userID The ID of the user whose logs are to be fetched.
     * @return List of logs associated with the user.
     */
    public List<String> getLogsByUserID(int userID) {
        String query = "SELECT LogID, Message, Timestamp FROM Logs WHERE UserID = ? ORDER BY Timestamp DESC";
        List<String> logs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int logID = rs.getInt("LogID");
                    String message = rs.getString("Message");
                    Timestamp timestamp = rs.getTimestamp("Timestamp");

                    // Format log message with user-specific details
                    logs.add(String.format("LogID: %d | Timestamp: %s | Message: %s",
                            logID, timestamp.toString(), message));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching logs for userID " + userID + ": " + e.getMessage());
        }
        return logs;
    }

    
    
    

    // ---------------- Support Query Management ---------------- //

    /**
     * Saves a new support query to the database.
     *
     * @param supportQuery The query to save.
     * @return true if the query was successfully saved.
     */
    public boolean saveSupportQuery(SupportQuery supportQuery) {
        String query = "INSERT INTO SupportQueries (UserId, QueryText, QueryDate, Resolved) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, supportQuery.getUserId());
            stmt.setString(2, supportQuery.getQueryText());
            stmt.setTimestamp(3, new Timestamp(supportQuery.getQueryDate().getTime())); // Convert Date to Timestamp
            stmt.setBoolean(4, supportQuery.isResolved());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving support query: " + e.getMessage());
            return false;
        }
    }
    	
    

    /**
     * Fetches all support queries in the database.e
     *
     * @return List of all support queries.
     */
    public List<SupportQuery> getAllSupportQueries() {
        String query = "SELECT * FROM SupportQueries";
        List<SupportQuery> supportQueries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                supportQueries.add(createSupportQueryFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all support queries: " + e.getMessage());
        }
        return supportQueries;
    }
    

    
    

    /**
     * Fetches unresolved support queries.
     *
     * @return List of unresolved support queries.
     */
    public List<SupportQuery> getUnresolvedSupportQueries() {
        String query = "SELECT * FROM SupportQueries WHERE Resolved = 0";
        List<SupportQuery> supportQueries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                supportQueries.add(createSupportQueryFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unresolved support queries: " + e.getMessage());
        }
        return supportQueries;
    }


    

    /**
     * Resolves a support query by its ID.
     *
     * @param queryId The ID of the query to resolve.
     * @return true if the query was resolved successfully, false otherwise.
     */
    public boolean resolveSupportQuery(int queryId) {
        String query = "UPDATE SupportQueries SET Resolved = 1 WHERE QueryId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, queryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error resolving support query: " + e.getMessage());
            return false;
        }
    }

    

    /**
     * Deletes a support query by its ID.
     *
     * @param queryId The ID of the query to delete.
     * @return true if the query was deleted successfully.
     */
    public boolean deleteSupportQuery(int queryId) {
        String query = "DELETE FROM SupportQueries WHERE QueryId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, queryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting support query: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetches support queries submitted by a specific user.
     *
     * @param userId The user ID.
     * @return List of support queries submitted by the user.
     */
    public List<SupportQuery> getSupportQueriesByUserId(int userId) {
        String query = "SELECT * FROM SupportQueries WHERE UserId = ?";
        List<SupportQuery> supportQueries = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    supportQueries.add(createSupportQueryFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching support queries by user ID: " + e.getMessage());
        }
        return supportQueries;
    }
    private SupportQuery createSupportQueryFromResultSet(ResultSet rs) throws SQLException {
        int queryId = rs.getInt("QueryId");
        int userId = rs.getInt("UserId");
        String queryText = rs.getString("QueryText");
        Date queryDate = rs.getTimestamp("QueryDate"); // Convert Timestamp to Date
        boolean resolved = rs.getBoolean("Resolved");
        SupportQuery supportQuery = new SupportQuery(userId, queryText);
        supportQuery.setQueryId(queryId); // Set the auto-generated ID
        supportQuery.setQueryDate(queryDate); // Set the query date
        supportQuery.setResolved(resolved); // Set the resolved status
        return supportQuery;
    }
    /**
     * Retrieves a specific support query by its ID.
     *
     * @param queryId The ID of the support query to fetch.
     * @return An Optional containing the SupportQuery if found, otherwise Optional.empty().
     */
    public Optional<SupportQuery> getSupportQueryById(int queryId) {
        String query = "SELECT * FROM SupportQueries WHERE QueryId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, queryId); // Bind the query ID to the SQL statement

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createSupportQueryFromResultSet(rs)); // Map the result to a SupportQuery
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching support query by ID: " + e.getMessage());
        }
        return Optional.empty(); // Return empty if the query is not found
    }
    
}

//give me sql tables sql server of the database of user, job, application, notification, logs, support queries









