package lt.swedbank.exceptions.userSkillLevel;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

/**
 * Created by Danielius on 20/06/2017.
 */
public class UserSkillLevelIsPendingException extends MainException {

    public UserSkillLevelIsPendingException() {
        this.messageCode = "user_skill_level_is_pending";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
