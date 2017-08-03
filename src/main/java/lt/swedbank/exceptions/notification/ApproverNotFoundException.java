package lt.swedbank.exceptions.notification;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class ApproverNotFoundException extends MainException {
    public ApproverNotFoundException() {
        this.messageCode = "approver_not_found";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
