package lt.swedbank.exceptions.skill;


import lt.swedbank.exceptions.ApplicationException;

public class SkillAlreadyExistsException extends ApplicationException {

    public SkillAlreadyExistsException(String errorCause) {
        super(errorCause);
    }

}

