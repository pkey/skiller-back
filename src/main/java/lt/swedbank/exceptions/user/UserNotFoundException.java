package lt.swedbank.exceptions.user;


public class UserNotFoundException extends RuntimeException{

    @Override
    public String getMessage() {
        return "User was not found";
    }
}
