package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
    UserEntityResponse getUser(@RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        return new UserEntityResponse(userService.getUserByAuthId(authId));
    }

    @RequestMapping(produces = "application/json", value = "/skill/add", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse addUserSkill(@Valid @RequestBody AddSkillRequest addSkillRequest,
                                    @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        userService.addUserSkill(userId, addSkillRequest);
        User userFromRepository = userService.getUserById(userId);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(produces = "application/json", value = "/skill/remove", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse removeUserSkill(@Valid @RequestBody RemoveSkillRequest removeSkillRequest,
                                       @RequestHeader(value = "Authorization") String token) {
        String authId = authService.extractAuthIdFromToken(token);
        Long userId = userService.getUserByAuthId(authId).getId();
        userService.removeUserSkill(userId, removeSkillRequest);
        User userFromRepository = userService.getUserById(userId);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<UserEntityResponse> getAllUsers(@RequestHeader(value = "Authorization") String authToken) {
        return userService.getUserEntityResponseList();
    }

}
