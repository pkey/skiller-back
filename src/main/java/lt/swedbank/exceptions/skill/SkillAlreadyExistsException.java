package lt.swedbank.exceptions.skill;


import lt.swedbank.exceptions.MainException;

public class SkillAlreadyExistsException extends MainException {

    private static final String MESSAGE_CODE = "skill_already_exists";

    public SkillAlreadyExistsException() {
        this.messageCode = MESSAGE_CODE;
    }
}


