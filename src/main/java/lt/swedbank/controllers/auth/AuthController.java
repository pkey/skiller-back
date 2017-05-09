package lt.swedbank.controllers.auth;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import lt.swedbank.beans.Skill;
import lt.swedbank.beans.User;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserRepository;
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

import java.util.LinkedList;
import java.util.List;


@Controller
@Component
@CrossOrigin(origins = "*")
public class AuthController {

    private AuthenticationService authService;

    @Autowired
    public void setAuthenticationService(Auth0AuthenticationService authService) {
        this.authService = authService;
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillRepository skillRepository;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> login(@RequestBody User user) {

        try {
            TokenHolder token = authService.loginUser(user);
            return new ResponseEntity<Object>(token, HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Auth0Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> register(@RequestBody User user) {

        try {
            User registeredUser = authService.registerUser(user);
            return new ResponseEntity<Object>(registeredUser, HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Auth0Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

            return new ResponseEntity<Object>(user, HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Auth0Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
