package lt.swedbank.exceptions.vote;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class VoteForHimselfException extends MainException {

    public VoteForHimselfException() {
        this.messageCode = "vote_for_himself";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
