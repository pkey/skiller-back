package lt.swedbank.controllers.skill;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.request.VoteUserSkillRequest;
import lt.swedbank.beans.response.VoteResponse;
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

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/user/skill")
public class UserSkillController {

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

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    UserSkillResponse addUserSkill(@Valid @RequestBody AddSkillRequest addSkillRequest,
                                   @RequestHeader(value = "Authorization") String authToken) {
        Long userId = userService.getUserByAuthId(authService.extractAuthIdFromToken(authToken)).getId();
        return userSkillService.addUserSkill(userId, addSkillRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    UserSkillResponse removeUserSkill(@Valid @RequestBody RemoveSkillRequest removeSkillRequest,
                                      @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        User user = userService.getUserByAuthId(authId);
        return userSkillService.removeUserSkill(user.getId(), removeSkillRequest);
    }

    @RequestMapping(value = "/level", method = RequestMethod.POST)
    public @ResponseBody
    UserSkillResponse assignUserSkillLevel(@Valid @RequestBody AssignSkillLevelRequest request,
                                           @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();

        ApprovalRequest approvalRequest = approvalService.addSkillLevelApprovalRequestWithNotifications(userId, request);
        UserSkill userSkill = approvalRequest.getUserSkillLevel().getUserSkill();

        return new NormalUserSkillResponse(userSkill.getSkill(), userSkillService.getCurrentSkillLevelStatus(userSkill));
    }

    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public @ResponseBody
    VoteResponse voteUserSkillLevel(@Valid @RequestBody VoteUserSkillRequest request,
                                    @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return voteService.voteUserSkill(request, userId);
    }
}
