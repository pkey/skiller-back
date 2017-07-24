package lt.swedbank.exceptions.team;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class TeamNameAlreadyExistsException extends MainException {

    public TeamNameAlreadyExistsException() {
        this.messageCode = "team_name_exists";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
