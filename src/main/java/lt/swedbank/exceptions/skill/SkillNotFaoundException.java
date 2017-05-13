package lt.swedbank.exceptions.skill;

public class SkillNotFaoundException extends Exception {
    @Override
    public String getMessage()
    {
        return "Skill not found!";
    }
}
