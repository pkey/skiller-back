package lt.swedbank.exceptions.notification;


import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class NoSuchNotificationException extends MainException{

    public NoSuchNotificationException() {
        this.messageCode = "no_such_notification";
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
}
