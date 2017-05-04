package lt.swedbank.controllers.user;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import lt.swedbank.beans.User;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserService userService;

    @Autowired
    public void setAuthenticationService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(produces = "application/json", value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getUser(@RequestHeader(value="Authorization") String token) {
        try {
            User user = userService.getUserByToken(token);
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        } catch (APIException exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Auth0Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
