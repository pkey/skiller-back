package lt.swedbank.controllers;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.auth0.net.SignUpRequest;
import org.hibernate.loader.custom.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Component
public class MainController {


    AuthAPI auth = new AuthAPI("https://skiller.eu.auth0.com/",
            "O6JkkKHyKfujkLjALIEAEYONE0XFatb8",
            "t4-jBn57is-WeG71RwW7UOa69cvxbkqbihx14zmwHor4gU4ztWMZ4K9u8yaZphYP");



    @RequestMapping(value = "/login")
    public @ResponseBody
    String login() {
        return "All good. You DO NOT need to be authenticated to call /login";
    }

    //AuthRequest login(String emailOrUsername, String password)
    /* Maps to all HTTP actions by default (GET,POST,..)*/
    @RequestMapping("/register")
    public @ResponseBody
    String register() {


        Map<String, String> fields = new HashMap<>();
        fields.put("age", "25");
        fields.put("city", "Buenos Aires");
        SignUpRequest request = auth.signUp("user@domain.com", "username", "password123", "Username-Password-Authentication")
                .setCustomFields(fields);
        try {
            request.execute();
        } catch (APIException exception) {
            return exception.getMessage();
        } catch (Auth0Exception exception) {
            return exception.getMessage();
        }

        return "Registration successful";
    }


    @RequestMapping("/get")
    public @ResponseBody
    String get() {
        return "Should only be returned to auhtorized person";
    }


}
