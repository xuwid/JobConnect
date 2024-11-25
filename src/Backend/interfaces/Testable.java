package Backend.interfaces;

public interface Testable {
    void takeMCQTest(long testId);
    void viewTestResults(long userId);
    String getRecommendations();
}
