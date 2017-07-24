package lt.swedbank.controllers.team;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.request.AddTeamRequest;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.team.teamOverview.TeamOverviewResponse;
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
    TeamOverviewResponse getTeamOverview(@RequestHeader(value = "Authorization") String authToken,
                                         @PathVariable("id") Long id) {
        String authId = authenticationService.extractAuthIdFromToken(authToken);
        Long userId = userService.getUserByAuthId(authId).getId();
        return teamService.getTeamOverview(id, userId);
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public @ResponseBody
    TeamOverviewResponse getMyTeam(@RequestHeader(value = "Authorization") String authToken) {
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
    public TeamResponse addTeam(@RequestBody AddTeamRequest addTeamRequest) {
        return teamService.addTeam(addTeamRequest);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<TeamOverviewResponse> getAllTeams() {
        return teamService.getAllTeamOverviewResponses();
    }




}
