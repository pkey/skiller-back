package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    UserEntityResponse getUser(@RequestHeader(value = "Authorization") String authToken) {
        return new UserEntityResponse(userService.getUserByAuthenticationToken(authToken));
    }

    @RequestMapping(produces = "application/json", value = "/skill/add", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addUserSkill(@Valid @RequestBody AddSkillRequest addSkillRequest,
                                   @RequestHeader(value = "Authorization") String authToken) {
        try {
            Long id = userService.getUserByAuthenticationToken(authToken).getId();
            userService.addUserSkill(id, addSkillRequest);
            User userFromRepository = userService.getUserById(id);
            return new ResponseEntity<UserEntityResponse>(new UserEntityResponse(userFromRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(produces = "application/json", value = "/skill/remove", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> removeUserSkill(@Valid @RequestBody RemoveSkillRequest removeSkillRequest,
                                      @RequestHeader(value = "Authorization") String authToken) {
        try {
            Long id = userService.getUserByAuthenticationToken(authToken).getId();
            userService.removeUserSkill(id, removeSkillRequest);
            User userFromRepository = userService.getUserById(id);
            return new ResponseEntity<UserEntityResponse>(new UserEntityResponse(userFromRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
