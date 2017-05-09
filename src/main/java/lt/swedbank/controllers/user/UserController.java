package lt.swedbank.controllers.user;

import lt.swedbank.beans.User;
import lt.swedbank.services.user.IUserService;
import lt.swedbank.services.user.UserService;
import org.hibernate.validator.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    private IUserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getUser(@RequestAttribute(value = "email") @Email(message = "Not an email") String email) {

        User userFromRepository = userService.getUserByEmail(email);
        return new ResponseEntity<Object>(userFromRepository, HttpStatus.OK);

    }
}
