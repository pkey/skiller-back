package lt.swedbank.services.auth;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import lt.swedbank.beans.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    User registerUser(RegisterUserRequest user) throws APIException, Auth0Exception;

    TokenHolder loginUser(LoginUserRequest user) throws APIException, Auth0Exception;

    User getUser(String token) throws APIException, Auth0Exception;

}



