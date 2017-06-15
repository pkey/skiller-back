package lt.swedbank.exceptions.request;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class RequestAlreadySubmittedException extends MainException {

    public RequestAlreadySubmittedException() {
        this.messageCode = "request_already_submitted";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
