package lt.swedbank.services;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import lt.swedbank.beans.User;
import org.springframework.stereotype.Service;

/**
 * Created by paulius on 4/24/17.
 */

@Service
public interface AuthenticationService  {

    void registerUser(User user) throws APIException, Auth0Exception;

    void loginUser(User user);
    }

