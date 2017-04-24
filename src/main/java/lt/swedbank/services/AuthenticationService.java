package lt.swedbank.services;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import lt.swedbank.beans.User;
import org.springframework.stereotype.Service;

/**
 * Created by paulius on 4/24/17.
 */

@Service
public interface AuthenticationService  {

    User registerUser(User user) throws APIException, Auth0Exception;

    TokenHolder loginUser(User user) throws APIException, Auth0Exception ;
    }

