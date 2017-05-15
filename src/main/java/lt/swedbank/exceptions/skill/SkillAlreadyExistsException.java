package lt.swedbank.exceptions.skill;


public class SkillAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User already has this skill!";
    }

}

