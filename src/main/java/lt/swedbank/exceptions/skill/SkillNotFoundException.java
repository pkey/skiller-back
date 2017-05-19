package lt.swedbank.exceptions.skill;

import lt.swedbank.exceptions.ApplicationException;

public class SkillNotFoundException extends ApplicationException {
    public SkillNotFoundException(String errorCause) {
        super(errorCause);
    }
}
