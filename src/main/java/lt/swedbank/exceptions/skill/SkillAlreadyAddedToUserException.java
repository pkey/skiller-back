package lt.swedbank.exceptions.skill;


public class SkillAlreadyAddedToUserException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Skill already added to the user";
    }

}

