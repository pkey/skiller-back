package lt.swedbank.controllers.user;

import lt.swedbank.beans.User;
import lt.swedbank.beans.response.UserResponse;
import lt.swedbank.services.user.IUserService;
import lt.swedbank.services.user.UserService;
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
    UserResponse getUser(@RequestAttribute(value = "email") String email) {
        return new UserResponse(userService.getUserByEmail(email));
    }
}
