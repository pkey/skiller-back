package lt.swedbank.beans.response;

import java.util.ArrayList;
import java.util.List;


public class AuthenticationErrorsWrapper extends Response {

    private List<AuthenticationError> errors;

    public AuthenticationErrorsWrapper() {
        this.errors = new ArrayList<AuthenticationError>();
    }

    public List<AuthenticationError> getErrors () {
        return errors;
    }

    public void addError(AuthenticationError error) {
        this.errors.add(error);
    }

}
