package lt.swedbank.services.auth;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;

public interface AuthenticationService {

    User registerUser(RegisterUserRequest user) throws Auth0Exception;

    TokenHolder loginUser(LoginUserRequest user) throws Auth0Exception;
}



