package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.services.user.UserService;
import org.hibernate.validator.constraints.Email;
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
    UserEntityResponse getUser(@RequestAttribute(value = "email") String email) {
        return new UserEntityResponse(userService.getUserByEmail(email));
    }

    @RequestMapping(produces = "application/json", value = "/skill/add", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addUserSkill(@RequestAttribute(value = "email") @Email(message = "Not an email") String email,
                                   @Valid @RequestBody AddSkillRequest addSkillRequest) {
        try {
            userService.addUserSkill(email, addSkillRequest);

            User userFromRepository = userService.getUserByEmail(email);
            return new ResponseEntity<UserEntityResponse>(new UserEntityResponse(userFromRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(produces = "application/json", value = "/skill/remove", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> removeUserSkill(@RequestAttribute(value = "email") @Email(message = "Not an email") String email,
                                     @Valid @RequestBody RemoveSkillRequest removeSkillRequest) {
        try {
            userService.removeUserSkill(email, removeSkillRequest);

            User userFromRepository = userService.getUserByEmail(email);
            return new ResponseEntity<UserEntityResponse>(new UserEntityResponse(userFromRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
