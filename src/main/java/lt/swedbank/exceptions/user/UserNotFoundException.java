package lt.swedbank.exceptions.user;


import lt.swedbank.exceptions.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String errorCause) {
        super(errorCause);
    }
}


