package lt.swedbank.exceptions.skill;

import lt.swedbank.exceptions.MainException;

public class SkillNotFoundException extends MainException {
    private static final String MESSAGE_CODE = "skill_not_found";

    public SkillNotFoundException() {
        this.messageCode = MESSAGE_CODE;
    }

}
