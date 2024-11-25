package Backend.interfaces;

public interface JobActions {
    void createJob(String title, String description, String salary, String location, String[] requirements);
    void updateJob(long jobId, String title, String description, String salary, String location, String[] requirements);
    void deleteJob(long jobId);
    void viewJobDetails(long jobId);
}
