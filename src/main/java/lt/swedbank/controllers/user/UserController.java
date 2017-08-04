package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private UserSkillService userSkillService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    UserResponse getMyProfile(@RequestHeader(value = "Authorization") String authToken) {
        return userService.getMyProfile(authService.extractAuthIdFromToken(authToken));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    UserResponse getUserProfile(@RequestHeader(value = "Authorization") String authToken,
                                @PathVariable("id") Long id) {
        String authId = authService.extractAuthIdFromToken(authToken);
        return userService.getUserProfile(id, authId);
    }

    @RequestMapping("/search")
    public List<UserResponse> searchColleagues(@RequestHeader(value = "Authorization") String authToken,
                                                     String q) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return userService.searchColleagues(userId, q);
    }

    @RequestMapping(value = "/team", method = RequestMethod.PUT)
    public @ResponseBody
    UserWithSkillsResponse assignUserTeam(@RequestHeader(value = "Authorization") String authToken,
                                          @RequestBody AssignTeamRequest assignTeamRequest) {
        String authId = authService.extractAuthIdFromToken(authToken);
        User user = userService.getUserByAuthId(authId);
        userService.assignTeam(user.getId(), assignTeamRequest);
        return new UserWithSkillsResponse(user, userSkillService.getNormalUserSkillResponseList(user.getUserSkills()));
    }

}
