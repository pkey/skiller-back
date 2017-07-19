package lt.swedbank.FakeAuthClasses;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

/**
 * Created by p998zwb on 2017.07.19.
 */
public class UserAlreaduExistException extends MainException {
    public UserAlreaduExistException() {
        this.messageCode = "user_already_exist";
        this.statusCode = HttpStatus.NOT_FOUND;
    }

}
