package lt.swedbank.services;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.auth0.net.SignUpRequest;
import lt.swedbank.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by paulius on 4/24/17.
 */
@Service
public class Auth0AuthenticationService implements AuthenticationService {


    private String clientId;

    private String clientSecret;

    private String clientDomain;

    private AuthAPI auth;

    @Autowired
    public Auth0AuthenticationService(@Value("${auth0.clientId}") String clientId,
                                      @Value("${auth0.clientSecret}") String clientSecret,
                                      @Value("${auth0.clientDomain}") String clientDomain) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.clientDomain = clientDomain;

            this.auth = new AuthAPI(clientDomain, clientId, clientSecret);
    }




    @Override
    public User registerUser(User user) throws APIException, Auth0Exception {

        Map<String, String> fields = new HashMap<>();
        fields.put("name", user.getName());
        fields.put("lastName", user.getLastName());

        SignUpRequest request = auth.signUp(user.getEmail(), user.getEmail(), user.getPassword(), user.getConnection())
                .setCustomFields(fields);

        request.execute();

        return user;

    }

    @Override
    public TokenHolder loginUser(User user) throws APIException, Auth0Exception {
        AuthRequest request = auth.login(user.getEmail(), user.getPassword(), user.getConnection())
                .setAudience("https://skiller/api")
                .setScope("openid");

        TokenHolder holder = request.execute();

        return holder;
    }
}
