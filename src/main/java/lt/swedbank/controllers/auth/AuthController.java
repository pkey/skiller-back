package lt.swedbank.controllers.auth;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import io.swagger.annotations.Api;
import lt.swedbank.beans.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;
import lt.swedbank.beans.response.RegisterUserResponse;
import lt.swedbank.services.auth.Auth0AuthenticationService;
import lt.swedbank.services.auth.AuthenticationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Component
@CrossOrigin(origins = "*")
public class AuthController {

    private AuthenticationService authService;

    @Autowired
    public void setAuthenticationService(Auth0AuthenticationService authService) {
        this.authService = authService;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> login(@Valid @RequestBody LoginUserRequest user) throws APIException, Auth0Exception {
        TokenHolder token = authService.loginUser(user);
        return new ResponseEntity<Object>(token, HttpStatus.OK);
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest user) throws APIException, Auth0Exception {
        User registeredUser = authService.registerUser(user);
        return new ResponseEntity<Object>(registeredUser, HttpStatus.OK);
    }


    @RequestMapping(produces = "application/json", value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getUser(@RequestHeader(value="Authorization") String token) {
        try {
            User user = authService.getUser(token);

            //hardcoded skills section
            JSONObject userJson = new JSONObject(user);

            JSONArray skills = new JSONArray();
            skills.put(new JSONObject().put("name", "java"));
            skills.put(new JSONObject().put("name", "something"));
            skills.put(new JSONObject().put("name", "Angular"));
            skills.put(new JSONObject().put("name", "Spring"));

            userJson.put("skills", skills);
            //

            return new ResponseEntity<Object>(/*user*/userJson.toString(), HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Auth0Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
