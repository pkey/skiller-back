package lt.swedbank.controllers;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.auth0.net.SignUpRequest;
import lt.swedbank.beans.User;
import lt.swedbank.services.Auth0AuthenticationService;
import lt.swedbank.services.AuthenticationService;
import org.hibernate.loader.custom.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Component
public class MainController {


    private AuthenticationService authService;

    @Autowired
    public void setAuthenticationService(Auth0AuthenticationService authService) {
        this.authService = authService;
    }


    @RequestMapping(value = "/login")
    public @ResponseBody
    ResponseEntity<String> login(@RequestBody User user) {
        try {
            TokenHolder token = authService.loginUser(user);
            return new ResponseEntity<String>(token.getAccessToken(), HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Auth0Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //AuthRequest login(String emailOrUsername, String password)
    /* Maps to all HTTP actions by default (GET,POST,..)*/
    @RequestMapping("/register")
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


    @RequestMapping("/get")
    public @ResponseBody
    String get() {
        return "Should only be returned to auhtorized person";
    }


}
