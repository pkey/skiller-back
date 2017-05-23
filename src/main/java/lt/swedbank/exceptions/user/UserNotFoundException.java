package lt.swedbank.exceptions.user;


import lt.swedbank.exceptions.MainException;

public class UserNotFoundException extends MainException {

    private static final String MESSAGE_CODE = "user_not_found";

    public UserNotFoundException() {
        this.messageCode = MESSAGE_CODE;
    }
}


