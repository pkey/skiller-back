package lt.swedbank.services.auth;


import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;
import lt.swedbank.repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Auth0AuthenticationService implements AuthenticationService {

    @Value("${auth0.scope}")
    private String scope;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.key.client.id}")
    private String keyClientId;
    @Value("${auth0.key.password}")
    private String keyPassword;
    @Value("${auth0.key.email}")
    private String keyEmail;
    @Value("${auth0.key.connection}")
    private String keyConnection;
    @Value("${auth0.key.name}")
    private String keyName;
    @Value("${auth0.key.name.last}")
    private String keyLastName;
    @Value("${auth0.key.userMetaData}")
    private String keyUserMetadata;

    @Value("${auth0.prefix.subject}")
    private String subjectPrefix;
    @Value("${auth0.prefix.token}")
    private String tokenPrefix;

    private String clientId; //Auth0 client ID

    private String clientSecret; //Auth0 client secret

    private String clientDomain; //Auth0 client domain

    private AuthAPI auth; //Auth0 Authentication API

    private UserRepository userRepository;

    @Autowired
    public Auth0AuthenticationService(@Value("${auth0.clientId}") String clientId,
                                      @Value("${auth0.clientSecret}") String clientSecret,
                                      @Value("${auth0.clientDomain}") String clientDomain,
                                      UserRepository userRepository) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientDomain = clientDomain;

        this.auth = new AuthAPI(clientDomain, clientId, clientSecret);

        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(final RegisterUserRequest registerUserRequest) throws Auth0Exception {

        HttpResponse<String> response;

        try {
            response = Unirest.post(this.clientDomain + "dbconnections/signup")
                    .header("content-type", "application/json")
                    .body(produceRegisterRequestBody(registerUserRequest))
                    .asString();
        } catch (UnirestException e) {
            throw new Auth0Exception(e.getMessage());
        }

        JSONObject responseBody = new JSONObject(response.getBody());

        if (response.getStatus() >= 400) {
            throw new Auth0Exception(responseBody.getString("description"));
        }

        String authId = responseBody.getString("_id");

        User user = new User(registerUserRequest);
        user.setAuthId(authId);
        userRepository.save(user);

        return user;
    }

    private JSONObject produceRegisterRequestBody(final RegisterUserRequest registerUserRequest) {
        Map<String, Object> requestBodyFields = new HashMap<>();
        requestBodyFields.put(keyClientId, this.clientId);
        requestBodyFields.put(keyEmail, registerUserRequest.getEmail());
        requestBodyFields.put(keyPassword, registerUserRequest.getPassword());
        requestBodyFields.put(keyConnection, registerUserRequest.getConnection());


        Map<String, String> fields = new HashMap<>();
        fields.put(keyName, registerUserRequest.getName());
        fields.put(keyLastName, registerUserRequest.getLastName());

        requestBodyFields.put(keyUserMetadata, fields);

        return new JSONObject(requestBodyFields);
    }

    @Override
    public TokenHolder loginUser(LoginUserRequest user) throws Auth0Exception {
        AuthRequest request = auth.login(user.getEmail(), user.getPassword(), user.getConnection())
                .setAudience(audience)
                .setScope(scope);

        TokenHolder holder = request.execute();

        return holder;
    }
}

