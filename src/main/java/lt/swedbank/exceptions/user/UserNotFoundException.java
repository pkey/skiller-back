package lt.swedbank.exceptions.user;


import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MainException {



    public UserNotFoundException() {
        this.messageCode = "user_not_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}


