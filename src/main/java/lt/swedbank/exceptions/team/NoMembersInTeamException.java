package lt.swedbank.exceptions.team;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class NoMembersInTeamException extends MainException {
    public NoMembersInTeamException(){
        this.messageCode = "team_members_not_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}
