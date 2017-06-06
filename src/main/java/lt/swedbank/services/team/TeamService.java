package lt.swedbank.services.team;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.TeamEntityResponse;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Iterable<TeamEntityResponse> getTeamEntityResponseList(){
        List<TeamEntityResponse> responseList = new ArrayList<>();
        for (Team team : getAllTeams()) {
            responseList.add(new TeamEntityResponse(team));
        }
        return responseList;
    }

    public Team getTeamById(Long id) {
        return teamRepository.findOne(id);
    }
}
