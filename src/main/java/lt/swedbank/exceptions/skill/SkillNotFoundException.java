package lt.swedbank.exceptions.skill;

public class SkillNotFoundException extends RuntimeException {
    @Override
    public String getMessage()
    {
        return "UserSkill not found!";
    }
}
