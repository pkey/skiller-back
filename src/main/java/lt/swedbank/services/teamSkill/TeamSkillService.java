package lt.swedbank.services.teamSkill;

import lt.swedbank.beans.entity.*;
import lt.swedbank.repositories.TeamSkillRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamSkillService {

    @Autowired
    private TeamSkillRepository teamSkillRepository;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserSkillService userSkillService;

    public List<TeamSkill> createTeamSkills(Team team) {
        List<TeamSkill> teamSkills = new ArrayList<>();
        for (Skill skill : team.getSkillTemplate().getSkills()) {
            TeamSkill teamSkill = new TeamSkill(team,
                    skill,
                    this.getUserSkillCount(team.getUsers(), skill),
                    this.getUserAverageSkillLevel(team.getUsers(), skill));

            teamSkills.add(teamSkill);
        }

        return (List<TeamSkill>) teamSkillRepository.save(teamSkills);
    }

    public List<TeamSkill> updateTeamSkills(@NotNull Team team) {
        List<TeamSkill> teamSkills = new ArrayList<>();

        for (Skill skill : team.getSkillTemplate().getSkills()) {
            if (teamSkillRepository.findTopByTeamAndSkill(team, skill) != null) {
                this.updateTeamSkill(team.getId(), skill);
            } else {
                TeamSkill teamSkill = new TeamSkill(team,
                        skill,
                        this.getUserSkillCount(team.getUsers(), skill),
                        this.getUserAverageSkillLevel(team.getUsers(), skill));
                teamSkills.add(teamSkill);
            }
        }

        return (List<TeamSkill>) teamSkillRepository.save(teamSkills);
    }

    public TeamSkill updateTeamSkill(@NotNull Long teamID, @NotNull Skill skill) {
        //Update creates new team skill in order to have a statistics on how they average and total changed
        Team team = teamService.getTeamById(teamID);

        TeamSkill teamSkill = new TeamSkill(team,
                skill,
                this.getUserSkillCount(team.getUsers(), skill),
                this.getUserAverageSkillLevel(team.getUsers(), skill));
        return teamSkillRepository.save(teamSkill);
    }

    public TeamSkill getCurrentTeamSkillByTeamAndSkill(@NotNull Team team, @NotNull Skill skill) {
        return teamSkillRepository.findTopByTeamAndSkill(team, skill);
    }

    public Double getUserAverageSkillLevel(List<User> users, Skill skill) {
        int counter = 0;
        double sum = 0;
        for (User user : users
                ) {
            for (UserSkill userSkill : user.getUserSkills()
                    ) {
                if (userSkill.getSkill().equals(skill)) {
                    counter++;
                    sum += userSkillService.getCurrentSkillLevel(userSkill).getSkillLevel().getLevel();
                }
            }
        }
        if (counter == 0) {
            return 0d;
        }
        return sum / counter;
    }

    public Integer getUserSkillCount(List<User> users, Skill skill) {
        int counter = 0;
        for (User user : users
                ) {
            for (UserSkill userSkill : user.getUserSkills()
                    ) {
                if (userSkill.getSkill().equals(skill)) {
                    counter++;
                }
            }
        }
        return counter;
    }
}
