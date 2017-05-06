package lt.swedbank.controllers.user;

import lt.swedbank.beans.User;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(produces = "application/json", value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getUser(@RequestAttribute(value = "email") String email) {
        try {
            User userFromRepository = userService.getUserByEmail(email);
            return new ResponseEntity<Object>(userFromRepository, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
