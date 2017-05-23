package lt.swedbank.exceptions.skill;


import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class SkillAlreadyExistsException extends MainException {


    public SkillAlreadyExistsException() {
        this.messageCode = "skill_already_exists";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}


