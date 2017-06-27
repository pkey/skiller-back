package lt.swedbank.controllers.team;

import lt.swedbank.beans.response.team.teamOverview.TeamOverviewResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.team.TeamService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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



}
