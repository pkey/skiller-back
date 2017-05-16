package lt.swedbank.services.auth;


import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.jwt.JWT;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@PropertySources({
        @PropertySource("classpath:auth0.properties")
})
public class Auth0AuthenticationService implements AuthenticationService {

    private static final String SCOPE = "openid";

    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CONNECTION = "connection";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_USER_METADATA = "user_metadata";

    private static final String SUBJECT_PREFIX = "auth0\\|";
    private static final String TOKEN_PREFIX = "Bearer ";

    private String clientId; //Auth0 client ID

    private String clientAudience; //Auth0 client secret

    private String clientDomain; //Auth0 client domain

    private AuthAPI auth; //Auth0 Authentication API

    private UserRepository userRepository;

    @Autowired
    public Auth0AuthenticationService(@Value("${auth0.clientId}") String clientId,
                                      @Value("${auth0.clientDomain}") String clientDomain,
                                      @Value("${auth0.clientSecret}") String clientSecret,
                                      @Value("${auth0.clientAudience") String clientAudience,
                                      UserRepository userRepository) {
        this.clientId = clientId;
        this.clientDomain = clientDomain;
        this.clientAudience =  clientAudience;

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
        requestBodyFields.put(KEY_CLIENT_ID, this.clientId);
        requestBodyFields.put(KEY_EMAIL, registerUserRequest.getEmail());
        requestBodyFields.put(KEY_PASSWORD, registerUserRequest.getPassword());
        requestBodyFields.put(KEY_CONNECTION, registerUserRequest.getConnection());


        Map<String, String> fields = new HashMap<>();
        fields.put(KEY_NAME, registerUserRequest.getName());
        fields.put(KEY_LAST_NAME, registerUserRequest.getLastName());

        requestBodyFields.put(KEY_USER_METADATA, fields);

        return new JSONObject(requestBodyFields);
    }

    @Override
    public TokenHolder loginUser(LoginUserRequest user) throws Auth0Exception {
        AuthRequest request = auth.login(user.getEmail(), user.getPassword(), user.getConnection())
                .setAudience(this.clientAudience)
                .setScope(SCOPE);

        TokenHolder holder = request.execute();

        return holder;
    }

    @Override
    public String extractAuthIdFromToken(String token) {
        return JWT.decode(removeTokenHead(token)).getSubject().replaceFirst(SUBJECT_PREFIX, "");
    }

    private String removeTokenHead(String token) {
        return token.replaceFirst(TOKEN_PREFIX, "");
    }


}

