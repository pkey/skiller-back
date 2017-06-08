package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.*;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.beans.response.VoteResponse;
import lt.swedbank.repositories.search.UserSearch;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.user.UserService;
import lt.swedbank.services.vote.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private UserSearch userSearch;
    @Autowired
    private VoteService voteService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public @ResponseBody
    UserEntityResponse getUser(@RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        return new UserEntityResponse(userService.getUserByAuthId(authId));
    }

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public @ResponseBody
    UserEntityResponse getUserProfile(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id) {
        return userService.getUserProfile(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<UserEntityResponse> getAllUsers(@RequestHeader(value = "Authorization") String authToken) {
        return (List<UserEntityResponse>) userService.getUserEntityResponseList();
    }

    @RequestMapping("/search")
    public List<UserEntityResponse> searchUsers(String q) {

        return sortUserEntityResponse(convertUserSetToUserResponseList(userSearch.search(q)));
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
        userService.removeUserSkill(userId, removeSkillRequest);
        User userFromRepository = userService.getUserById(userId);

        return new UserEntityResponse(userFromRepository);
    }

    @RequestMapping(value = "/skill/level", method = RequestMethod.POST)
    public @ResponseBody
    UserEntityResponse assignUserSkillLevel(@Valid @RequestBody AssignSkillLevelRequest request,
                                            @RequestHeader(value = "Authorization") String authToken) {
        String authId = authService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        userService.assignUserSkillLevel(userId, request);
        User userFromRepository = userService.getUserById(userId);

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



    private List<UserEntityResponse> convertUserSetToUserResponseList(Set<User> userList) {
        List<UserEntityResponse> responseList = new ArrayList<>();
        for (User user : userList) {
            responseList.add(new UserEntityResponse(user));
        }
        return responseList;
    }

    private List sortUserEntityResponse(List userEntityResponseList)
    {
        Collections.sort(userEntityResponseList);
        return userEntityResponseList;
    }

}
