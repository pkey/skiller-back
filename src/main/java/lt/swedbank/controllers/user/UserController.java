package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    UserEntityResponse getUser(Authentication authentication) {
        return new UserEntityResponse(userService.getUserByAuthId(authentication.getName()));
    }

    @RequestMapping(produces = "application/json", value = "/skill/add", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse addUserSkill(@Valid @RequestBody AddSkillRequest addSkillRequest,
                                    Authentication authentication) {
        Long userId = userService.getUserByAuthId(authentication.getName()).getId();
        userService.addUserSkill(userId, addSkillRequest);
        User userFromRepository = userService.getUserById(userId);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(produces = "application/json", value = "/skill/remove", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse removeUserSkill(@Valid @RequestBody RemoveSkillRequest removeSkillRequest,
                                       Authentication authentication) {
        Long userId = userService.getUserByAuthId(authentication.getName()).getId();
        userService.removeUserSkill(userId, removeSkillRequest);
        User userFromRepository = userService.getUserById(userId);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<UserEntityResponse> getAllUsers(@RequestHeader(value = "Authorization") String authToken) {
        return (List<UserEntityResponse>) userService.getUserEntityResponseList();
    }

}
