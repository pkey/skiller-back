package lt.swedbank.exceptions.skill;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class SkillNotFoundException extends MainException {

    public SkillNotFoundException() {
        this.messageCode = "skill_not_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }

}
