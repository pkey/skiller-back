package lt.swedbank.exceptions.userSkillLevel;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class TooHighSkillLevelRequestException extends MainException {

    public TooHighSkillLevelRequestException() {
        this.messageCode = "too_high_level";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
