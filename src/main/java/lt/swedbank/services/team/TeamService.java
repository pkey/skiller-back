package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.TeamOverviewResponse;
import lt.swedbank.exceptions.skillTemplate.NoSkillTemplateFoundException;
import lt.swedbank.exceptions.team.TeamNotFoundException;
import lt.swedbank.repositories.SkillTemplateRepository;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SkillTemplateRepository skillTemplateRepository;

    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team getTeamById(Long id) {
        Team team = teamRepository.findOne(id);
        if(team == null) {
            throw new TeamNotFoundException();
        }
        return team;
    }


    public TeamOverviewResponse getTeamOverview(Long teamId, Long currentUserId){
        User user = userService.getUserById(currentUserId);
        Team team = getTeamById(teamId);

        if(user.getTeam() == null)
        {
            return new NonColleagueTeamOverviewResponse(team);
        }

        if(user.getTeam().getDepartment().getId().equals(team.getDepartment().getId()))
            return new ColleagueTeamOverviewResponse(team);
        else
            return new NonColleagueTeamOverviewResponse(team);
    }


    public TeamOverviewResponse getMyTeam(Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        return new ColleagueTeamOverviewResponse(getTeamById(user.getTeam().getId()));
    }

    public SkillTemplate getTeamSkillTemplate(Team team)
    {
        return skillTemplateRepository.findOneByTeam(team);
    }

    public List<TeamSkillTemplateResponse> getTeamSkillTemplateResponseList(Team team){
        List<TeamSkillTemplateResponse> teamSkillTemplateResponseList = new ArrayList<>();
        if(getTeamSkillTemplate(team)== null)
        {
            throw new NoSkillTemplateFoundException();
        }
        for (Skill skill: getTeamSkillTemplate(team).getSkills()
             ) {
            TeamSkillTemplateResponse teamSkillTemplateResponse =
                    new TeamSkillTemplateResponse(skill, getSkillCountInTeam(team, skill), getAverageSkillLevelInTeam(team, skill));
            teamSkillTemplateResponseList.add(teamSkillTemplateResponse);
        }
        return teamSkillTemplateResponseList;
    }

    public double getAverageSkillLevelInTeam(Team team, Skill skill)
    {
        List<User> users = (List<User>) userService.getAllByTeam(team);
        int counter = 0;
        double sum = 0;
        for (User user: users
                ) {
            for (UserSkill userSkill: user.getUserSkills()
                    ) {
                if(userSkill.getSkill().equals(skill)) {
                    counter++;
                    sum+=userSkill.getCurrentSkillLevel().getSkillLevel().getLevel();
                }
            }
        }
        if(counter == 0)
        {
            return 0;
        }
        return sum/counter;
    }

    public int getSkillCountInTeam(Team team, Skill skill)
    {
        List<User> users = (List<User>) userService.getAllByTeam(team);
        int counter = 0;
        for (User user: users
             ) {
            for (UserSkill userSkill: user.getUserSkills()
                 ) {
                if(userSkill.getSkill().equals(skill)) {
                    counter++;
                }
            }
        }
        return counter;
    }

}
