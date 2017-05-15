package lt.swedbank.exceptions.skill;

public class SkillNotFoundException extends Exception {
    @Override
    public String getMessage()
    {
        return "Skill not found!";
    }
}
