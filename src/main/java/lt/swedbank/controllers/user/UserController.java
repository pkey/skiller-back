package lt.swedbank.controllers.user;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import lt.swedbank.beans.User;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.user.IUserService;
import lt.swedbank.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by paulius on 5/4/17.
 */
@Controller
@Component
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    private IUserService userService;
    private AuthenticationService authService;

    public UserController(UserService userService, AuthenticationService authService) {
        this.userService = userService;
        this.authService = authService;
    }


    @RequestMapping(produces = "application/json", value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getUser(@RequestHeader(value="Authorization") String token) {
        try {


            //TODO Fix the logic
            User userFromAuth0 = authService.getUser(token);

            User userFromRepository = userService.getUserByEmail(userFromAuth0.getEmail());

            return new ResponseEntity<Object>(userFromRepository, HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Auth0Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
