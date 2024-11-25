package Backend.entities;

public class Skill {
    private String skillId; // Unique identifier for the skill
    private String skillName; // Name of the skill

    public Skill(String skillId, String skillName, int skillLevel) {
        this.skillId = skillId;
        this.skillName = skillName;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }


    @Override
    public String toString() {
        return skillName ;
    }
}
