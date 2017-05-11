package lt.swedbank.controllers.user;


import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    UserEntityResponse getUser(@RequestAttribute(value = "email") String email) {
        return new UserEntityResponse(userService.getUserByEmail(email));
    }
}
