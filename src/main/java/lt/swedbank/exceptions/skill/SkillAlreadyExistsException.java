package lt.swedbank.exceptions.skill;


public class SkillAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Skill already exists on the user";
    }

}

