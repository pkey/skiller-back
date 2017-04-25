package lt.swedbank.services;

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
import lt.swedbank.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import java.util.HashMap;
import java.util.Map;

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




    ManagementAPI mgmt = new ManagementAPI("https://skiller.eu.auth0.com/", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik9UbEZNekl6TVRCRVF6Z3lRakpDUmpRek5UQTVOalZETmpFM05qWXlRalU1TWpGQ056WXpSZyJ9.eyJpc3MiOiJodHRwczovL3NraWxsZXIuZXUuYXV0aDAuY29tLyIsInN1YiI6Im9jOTZSQmpCNERtU2pYUnQ2MzN1MHFzbVQ3NUJrb1JHQGNsaWVudHMiLCJhdWQiOiJodHRwczovL3NraWxsZXIuZXUuYXV0aDAuY29tL2FwaS92Mi8iLCJleHAiOjE0OTMyMTg0NjUsImlhdCI6MTQ5MzEzMjA2NSwic2NvcGUiOiJyZWFkOmNsaWVudF9ncmFudHMgY3JlYXRlOmNsaWVudF9ncmFudHMgZGVsZXRlOmNsaWVudF9ncmFudHMgdXBkYXRlOmNsaWVudF9ncmFudHMgcmVhZDp1c2VycyB1cGRhdGU6dXNlcnMgZGVsZXRlOnVzZXJzIGNyZWF0ZTp1c2VycyByZWFkOnVzZXJzX2FwcF9tZXRhZGF0YSB1cGRhdGU6dXNlcnNfYXBwX21ldGFkYXRhIGRlbGV0ZTp1c2Vyc19hcHBfbWV0YWRhdGEgY3JlYXRlOnVzZXJzX2FwcF9tZXRhZGF0YSBjcmVhdGU6dXNlcl90aWNrZXRzIHJlYWQ6Y2xpZW50cyB1cGRhdGU6Y2xpZW50cyBkZWxldGU6Y2xpZW50cyBjcmVhdGU6Y2xpZW50cyByZWFkOmNsaWVudF9rZXlzIHVwZGF0ZTpjbGllbnRfa2V5cyBkZWxldGU6Y2xpZW50X2tleXMgY3JlYXRlOmNsaWVudF9rZXlzIHJlYWQ6Y29ubmVjdGlvbnMgdXBkYXRlOmNvbm5lY3Rpb25zIGRlbGV0ZTpjb25uZWN0aW9ucyBjcmVhdGU6Y29ubmVjdGlvbnMgcmVhZDpyZXNvdXJjZV9zZXJ2ZXJzIHVwZGF0ZTpyZXNvdXJjZV9zZXJ2ZXJzIGRlbGV0ZTpyZXNvdXJjZV9zZXJ2ZXJzIGNyZWF0ZTpyZXNvdXJjZV9zZXJ2ZXJzIHJlYWQ6ZGV2aWNlX2NyZWRlbnRpYWxzIHVwZGF0ZTpkZXZpY2VfY3JlZGVudGlhbHMgZGVsZXRlOmRldmljZV9jcmVkZW50aWFscyBjcmVhdGU6ZGV2aWNlX2NyZWRlbnRpYWxzIHJlYWQ6cnVsZXMgdXBkYXRlOnJ1bGVzIGRlbGV0ZTpydWxlcyBjcmVhdGU6cnVsZXMgcmVhZDplbWFpbF9wcm92aWRlciB1cGRhdGU6ZW1haWxfcHJvdmlkZXIgZGVsZXRlOmVtYWlsX3Byb3ZpZGVyIGNyZWF0ZTplbWFpbF9wcm92aWRlciBibGFja2xpc3Q6dG9rZW5zIHJlYWQ6c3RhdHMgcmVhZDp0ZW5hbnRfc2V0dGluZ3MgdXBkYXRlOnRlbmFudF9zZXR0aW5ncyByZWFkOmxvZ3MgcmVhZDpzaGllbGRzIGNyZWF0ZTpzaGllbGRzIGRlbGV0ZTpzaGllbGRzIHVwZGF0ZTp0cmlnZ2VycyByZWFkOnRyaWdnZXJzIHJlYWQ6Z3JhbnRzIGRlbGV0ZTpncmFudHMgcmVhZDpndWFyZGlhbl9mYWN0b3JzIHVwZGF0ZTpndWFyZGlhbl9mYWN0b3JzIHJlYWQ6Z3VhcmRpYW5fZW5yb2xsbWVudHMgZGVsZXRlOmd1YXJkaWFuX2Vucm9sbG1lbnRzIGNyZWF0ZTpndWFyZGlhbl9lbnJvbGxtZW50X3RpY2tldHMgcmVhZDp1c2VyX2lkcF90b2tlbnMifQ.jd_XdRzNhxXPIUTgLJ_iHaUIbWALOkECROoFs2AnmLcWcCuyCGzwvKxY5d1KslyZB8vqagCHGXltYLzAOeJfp6SljlOYKUqLiKFtacKbhe5zFcDKVJJWMGgcPK9FUTcHAXhbqhFCV-ku7XE2UE5uOwvGm-bBN2_CnklZFFL7obUcbiE5xlHUSiVLjluYlu0WqN3p3J9W7M-g1Gg7VeFEKpdke5vXGzmdjuepGbGfG7YIErQGVXuILhVWiLyfGWx7JBbZ0Eh7Ok_1VMgoaJXjACiT-x4XrmzAL9ZbmPNsqFlRCj-v225Ktz6Xl-Uhv3ojFrfDuDvI96zzL1dDzgygCw");


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
    public User getUser(String token)throws APIException, Auth0Exception  {
        UserInfo info = getUserInfo(removeTokenHead(token));
        return  parseUserInfoToUser(info);
    }

    private UserInfo getUserInfo(String token) throws Auth0Exception {
        Request<UserInfo> request = auth.userInfo(token);
        UserInfo info = request.execute();
        return info;
    }

    private String removeTokenHead(String token){
        return token.substring(7);
    }

    @Override
    public User parseUserInfoToUser(UserInfo userInfo) throws Auth0Exception {

        User user = new User();

        Map<String, Object> mappedUserInfo = userInfo.getValues();
        com.auth0.json.mgmt.users.User MgmtUser = getManagementResponse(mappedUserInfo);

        user.setEmail((String)mappedUserInfo.get("email"));
        user.setLastName((String) MgmtUser.getUserMetadata().get("lastName"));
        user.setName((String)MgmtUser.getUserMetadata().get("name"));

        return user;
    }

    private com.auth0.json.mgmt.users.User getManagementResponse(Map<String, Object> mappedUserInfo) throws Auth0Exception {
        UserFilter filter = new UserFilter();
        Request<com.auth0.json.mgmt.users.User> request = mgmt.users().get(getUserID(mappedUserInfo), filter);
        com.auth0.json.mgmt.users.User response = request.execute();
        return response;
    }

    private String getUserID(Map<String, Object> mappedUserInfo)
    {
        String userID = (String)mappedUserInfo.get("sub");
        return userID;
    }

}

