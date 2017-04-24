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
    String login(User user) {
        return "All good. You DO NOT need to be authenticated to call /login";
    }

    //AuthRequest login(String emailOrUsername, String password)
    /* Maps to all HTTP actions by default (GET,POST,..)*/
    @RequestMapping("/register")
    public @ResponseBody
    String register(@RequestBody User user) {

        try {

            authService.registerUser(user);
        } catch (APIException exception) {

        } catch (Auth0Exception exception) {

        }


        return "Registration successful";
    }


    @RequestMapping("/get")
    public @ResponseBody
    String get() {
        return "Should only be returned to auhtorized person";
    }


}
