package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.*;
import lt.swedbank.beans.response.VoteResponse;
import lt.swedbank.beans.response.user.UserEntityResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.repositories.search.UserSearchRepository;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.notification.ApprovalService;
import lt.swedbank.services.user.UserService;
import lt.swedbank.services.vote.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserSearchRepository userSearchRepository;
    @Autowired
    private VoteService voteService;
    @Autowired
    private ApprovalService approvalService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    UserEntityResponse getUser(@RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        return new UserEntityResponse(userService.getUserByAuthId(authId));
    }

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
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


    @RequestMapping(value = "/skill/add", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse addUserSkill(@Valid @RequestBody AddSkillRequest addSkillRequest,
                                    @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        userService.addUserSkill(userId, addSkillRequest);

        User userFromRepository = userService.getUserById(userId);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(value = "/skill/remove", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse assignUserSkillLevel(@Valid @RequestBody RemoveSkillRequest removeSkillRequest,
                                            @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();

        return new UserEntityResponse(userService.removeUserSkill(userId, removeSkillRequest));
    }

    @RequestMapping(value = "/skill/level", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse assignUserSkillLevel(@Valid @RequestBody AssignSkillLevelRequest request,
                                            @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        User userFromRepository = userService.getUserById(userId);
        approvalService.createSkillLevelApprovalRequest(userId, request);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(value = "/skill/vote", method = RequestMethod.POST)
    public @ResponseBody
    VoteResponse voteUserSkillLevel(@Valid @RequestBody VoteUserSkillRequest request,
                                    @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return voteService.voteUserSkill(request, userId);
    }

    @RequestMapping(value = "/team", method = RequestMethod.PUT)
    public @ResponseBody
    UserEntityResponse assignUserTeam(@RequestHeader(value = "Authorization") String authToken,
                                      @RequestBody AssignTeamRequest assignTeamRequest) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return new UserEntityResponse(userService.assignTeam(userId, assignTeamRequest));
    }

}
