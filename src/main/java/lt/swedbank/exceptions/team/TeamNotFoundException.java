package lt.swedbank.exceptions.team;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends MainException {

    public TeamNotFoundException(){
        this.messageCode = "team_not_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}
