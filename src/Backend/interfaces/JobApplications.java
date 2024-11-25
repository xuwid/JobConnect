package Backend.interfaces;

public interface JobApplications {
    void applyForJob(long userId, long jobId);
    String checkApplicationStatus(long applicationId);
    void withdrawApplication(long applicationId);
}
