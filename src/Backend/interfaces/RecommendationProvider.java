package Backend.interfaces;

public interface RecommendationProvider {
    void generateRecommendations(long userId);
    String[] getRecommendedCourses(long userId);
}
