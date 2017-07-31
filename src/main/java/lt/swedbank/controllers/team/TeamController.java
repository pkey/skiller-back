package lt.swedbank.controllers.team;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.request.team.AddTeamRequest;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.team.TeamService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/team")
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    TeamWithUsersResponse getTeamOverview(@RequestHeader(value = "Authorization") String authToken,
                                          @PathVariable("id") Long id) {
        String authId = authenticationService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return teamService.getTeamOverview(id, userId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    TeamWithUsersResponse updateTeam(@RequestHeader(value = "Authorization") String authToken,
                                          @PathVariable("id") Long id) {
        String authId = authenticationService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return teamService.getTeamOverview(id, userId);
    }


    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public @ResponseBody
    TeamWithUsersResponse getMyTeam(@RequestHeader(value = "Authorization") String authToken) {
        String authId = authenticationService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return teamService.getMyTeam(userId);
    }

    @RequestMapping(value = "/template/{teamId}", method = RequestMethod.GET)
    public @ResponseBody
    List<TeamSkillTemplateResponse> getTeamTemplate(@PathVariable("teamId") Long teamId)
    {
        Team team = teamService.getTeamById(teamId);
        List<TeamSkillTemplateResponse> templateResponse = teamService.getTeamSkillTemplateResponseList(team);
        Collections.sort(templateResponse);
        Collections.reverse(templateResponse);
        return templateResponse;
    }

    @RequestMapping(method = RequestMethod.POST)
    public TeamWithUsersResponse addTeam(@RequestBody AddTeamRequest addTeamRequest) {
        return teamService.addTeam(addTeamRequest);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<TeamWithUsersResponse> getAllTeams() {
        return teamService.getAllTeamOverviewResponses();
    }




}
