package lt.swedbank.controllers.user;

import lt.swedbank.beans.MyUserPrincipal;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    UserEntityResponse getUser(Principal principal) {
        principal.getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal myUserPrincipal = (MyUserPrincipal) authentication.getPrincipal();
        return new UserEntityResponse(userService.getUserByAuthId(principal.getUsername()));
    }

    @RequestMapping(produces = "application/json", value = "/skill/add", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse addUserSkill(@Valid @RequestBody AddSkillRequest addSkillRequest,
                                    Principal principal) {
        Long userId = userService.getUserByAuthId(principal.getName()).getId();
        userService.addUserSkill(userId, addSkillRequest);
        User userFromRepository = userService.getUserById(userId);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(produces = "application/json", value = "/skill/remove", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse removeUserSkill(@Valid @RequestBody RemoveSkillRequest removeSkillRequest,
                                       Principal principal) {
        Long userId = userService.getUserByAuthId(principal.getName()).getId();
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
