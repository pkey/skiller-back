package lt.swedbank.exceptions.notification;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class DisapproverNotFoundException extends MainException {
    public DisapproverNotFoundException() {
        this.messageCode = "disapprover_not_found";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
