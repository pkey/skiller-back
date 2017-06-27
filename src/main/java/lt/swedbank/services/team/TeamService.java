package lt.swedbank.services.team;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.TeamOverviewResponse;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserService userService;

    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        return teamRepository.findOne(id);
    }

    public TeamOverviewResponse getTeamOverview(Long teamId, Long currentUserId){
        User user = userService.getUserById(currentUserId);
        Team team = teamRepository.findOne(teamId);

        if(user.getTeam().getDepartment().getId().equals(team.getDepartment().getId()))
            return new ColleagueTeamOverviewResponse(team);
        else
            return new NonColleagueTeamOverviewResponse(team);
    }

    public TeamOverviewResponse getMyTeam(Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        Team team = teamRepository.findOne(user.getTeam().getId());

        return new ColleagueTeamOverviewResponse(team);
    }
}
