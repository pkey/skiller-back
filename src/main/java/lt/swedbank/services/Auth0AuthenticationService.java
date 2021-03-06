package lt.swedbank.services;


//Auth0 dependencies
import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.auth.UserInfo;
import com.auth0.net.AuthRequest;
import com.auth0.net.Request;
import com.auth0.net.SignUpRequest;

//Unirest to call rest services
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

import lt.swedbank.beans.User;


/**
 * Created by paulius on 4/24/17.
 */
@Service
public class Auth0AuthenticationService implements AuthenticationService {


    private String clientId; //Auth0 client ID

    private String clientSecret; //Auth0 client secret

    private String clientDomain; //Auth0 client domain

    private String managementApiAudience; //Auth0 client audience

    private AuthAPI auth; //Auth0 Authentication API

    private ManagementAPI mgmt; //Auth0 Management API

    @Autowired
    public Auth0AuthenticationService(@Value("${auth0.clientId}") String clientId,
                                      @Value("${auth0.clientSecret}") String clientSecret,
                                      @Value("${auth0.clientDomain}") String clientDomain,
                                      @Value("${auth0.managementApiAudience}") String managementApiAudience) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientDomain = clientDomain;
        this.managementApiAudience = managementApiAudience;


        this.auth = new AuthAPI(clientDomain, clientId, clientSecret);

        this.mgmt = new ManagementAPI(this.clientDomain, this.getApiAccessToken());
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

    @Override
    public User getUser(String token) throws APIException, Auth0Exception {
        UserInfo info = getUserInfo(removeTokenHead(token));
        return parseUserInfoToUser(info);
    }

    private UserInfo getUserInfo(String token) throws Auth0Exception {
        Request<UserInfo> request = auth.userInfo(token);
        UserInfo info = request.execute();
        return info;
    }

    private User parseUserInfoToUser(UserInfo userInfo) throws Auth0Exception {

        User user = new User();

        Map<String, Object> mappedUserInfo = userInfo.getValues();
        com.auth0.json.mgmt.users.User MgmtUser = getManagementResponse(mappedUserInfo);

        user.setEmail((String) mappedUserInfo.get("email"));
        user.setLastName((String) MgmtUser.getUserMetadata().get("lastName"));
        user.setName((String) MgmtUser.getUserMetadata().get("name"));

        return user;
    }

    private String removeTokenHead(String token) {
        return token.substring(7);
    }


    private com.auth0.json.mgmt.users.User getManagementResponse(Map<String, Object> mappedUserInfo) throws Auth0Exception {
        UserFilter filter = new UserFilter();
        Request<com.auth0.json.mgmt.users.User> request = mgmt.users().get(getUserID(mappedUserInfo), filter);
        com.auth0.json.mgmt.users.User response = request.execute();
        return response;
    }

    private String getUserID(Map<String, Object> mappedUserInfo) {
        String userID = (String) mappedUserInfo.get("sub");
        return userID;
    }

    private String getApiAccessToken() {


        try {

            Map<String, Object> requestBodyFields = new HashMap<>();
            requestBodyFields.put("grant_type", "client_credentials");
            requestBodyFields.put("client_id", this.clientId);
            requestBodyFields.put("client_secret", this.clientSecret);
            requestBodyFields.put("audience", this.managementApiAudience);

            JSONObject requestBody = new JSONObject(requestBodyFields);


            HttpResponse<String> response = Unirest.post("https://skiller.eu.auth0.com/oauth/token")
                    .header("content-type", "application/json")
                    .body(requestBody)
                    .asString();


            JSONObject myObject = new JSONObject(response.getBody());

            return myObject.getString("access_token");

        } catch (UnirestException u) {
            //TODO Handle Unirest exception
            return null;
        }
    }

}

