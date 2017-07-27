package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.*;
import lt.swedbank.beans.response.VoteResponse;
import lt.swedbank.beans.response.user.UserWithSkillsAndTeamResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.beans.response.userSkill.NormalUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.notification.ApprovalService;
import lt.swedbank.services.skill.UserSkillService;
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
    private UserSkillService userSkillService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private ApprovalService approvalService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    UserWithSkillsResponse getUser(@RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        return new UserWithSkillsAndTeamResponse(userService.getUserByAuthId(authId), userSkillService.getProfileUserSkills(userService.getUserByAuthId(authId).getUserSkills()));
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
    UserWithSkillsResponse addUserSkill(@Valid @RequestBody AddSkillRequest addSkillRequest,
                                        @RequestHeader(value = "Authorization") String authToken) {
        Long userId = userService.getUserByAuthId(authService.extractAuthIdFromToken(authToken)).getId();
        userService.addUserSkill(userId, addSkillRequest);
        return new UserWithSkillsResponse(userService.getUserById(userId), userSkillService.getNormalUserSkillResponseList(userService.getUserById(userId).getUserSkills()));
    }

    @RequestMapping(value = "/skill/remove", method = RequestMethod.POST)
    public @ResponseBody
    UserWithSkillsResponse assignUserSkillLevel(@Valid @RequestBody RemoveSkillRequest removeSkillRequest,
                                                @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
       User user = userService.getUserByAuthId(authId);

        return new UserWithSkillsResponse(user, userSkillService.getNormalUserSkillResponseList(user.getUserSkills()));
    }

    @RequestMapping(value = "/skill/level", method = RequestMethod.POST)
    public @ResponseBody
    UserSkillResponse assignUserSkillLevel(@Valid @RequestBody AssignSkillLevelRequest request,
                                           @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();

        ApprovalRequest approvalRequest = approvalService.addSkillLevelApprovalRequestWithNotifications(userId, request);
        UserSkill userSkill = approvalRequest.getUserSkillLevel().getUserSkill();

        return new NormalUserSkillResponse(userSkill.getSkill(), userSkillService.getCurrentSkillLevelStatus(userSkill));
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
    UserWithSkillsResponse assignUserTeam(@RequestHeader(value = "Authorization") String authToken,
                                          @RequestBody AssignTeamRequest assignTeamRequest) {
        String authId = authService.extractAuthIdFromToken(authToken);
        User user = userService.getUserByAuthId(authId);
        userService.assignTeam(user.getId(), assignTeamRequest);
        return new UserWithSkillsResponse(user, userSkillService.getNormalUserSkillResponseList(user.getUserSkills()));
    }

}
