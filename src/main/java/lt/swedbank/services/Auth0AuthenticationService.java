package lt.swedbank.services;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.net.SignUpRequest;
import lt.swedbank.beans.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by paulius on 4/24/17.
 */
@Service
public class Auth0AuthenticationService implements AuthenticationService  {


    AuthAPI auth = new AuthAPI("https://skiller.eu.auth0.com/",
            "O6JkkKHyKfujkLjALIEAEYONE0XFatb8",
            "t4-jBn57is-WeG71RwW7UOa69cvxbkqbihx14zmwHor4gU4ztWMZ4K9u8yaZphYP");

    @Override
    public void registerUser(User user) throws APIException, Auth0Exception {

        SignUpRequest request = auth.signUp(user.getEmail(), user.getUsername(), user.getPassword(), user.getConnection());

        request.execute();

    }

    @Override
    public void loginUser(User user) {

    }
}
