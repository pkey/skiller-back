package lt.swedbank.exceptions.skill;


import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class SkillLevelDoesNotExist extends MainException {

    public SkillLevelDoesNotExist() {
        this.messageCode = "skill_level_not_exist";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}
