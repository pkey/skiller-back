package lt.swedbank.services.unit;

import lt.swedbank.beans.entity.unit.Team;
import lt.swedbank.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Iterable<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) { return teamRepository.findOne(id); };
}
