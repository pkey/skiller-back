package lt.swedbank.services.teamSkill;

import lt.swedbank.beans.entity.*;
import lt.swedbank.repositories.TeamSkillRepository;
import lt.swedbank.services.skill.UserSkillService;
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
    private UserSkillService userSkillService;

    public void createTeamSkills(Team team) {
        List<TeamSkill> teamSkills = new ArrayList<>();
        for (Skill skill : team.getSkillTemplate().getSkills()) {
            TeamSkill teamSkill = new TeamSkill(team,
                    skill,
                    this.getUserSkillCount(team.getUsers(), skill),
                    this.getUserAverageSkillLevel(team.getUsers(), skill));

            teamSkills.add(teamSkill);
        }

        teamSkillRepository.save(teamSkills);
    }

    public void updateTeamSkills(@NotNull Team team) {
        for (Skill skill : team.getSkillTemplate().getSkills()) {
            this.updateTeamSkill(team, skill);
        }
    }

    public TeamSkill updateTeamSkill(@NotNull Team team, @NotNull Skill skill) {
        TeamSkill teamSkill = teamSkillRepository.findTopByTeamAndSkill(team, skill);

        if (teamSkill != null) {
            teamSkill.setSkillCount(this.getUserSkillCount(team.getUsers(), skill));
            teamSkill.setSkillLevelAverage(this.getUserAverageSkillLevel(team.getUsers(), skill));
        } else {
            teamSkill = new TeamSkill(team,
                    skill,
                    this.getUserSkillCount(team.getUsers(), skill),
                    this.getUserAverageSkillLevel(team.getUsers(), skill));
        }

        return teamSkillRepository.save(teamSkill);
    }

    public TeamSkill getCurrentTeamSkillByTeamAndSkill(@NotNull Team team, @NotNull Skill skill) {
        return teamSkillRepository.findTopByTeamAndSkill(team, skill);
    }

    private Double getUserAverageSkillLevel(@NotNull List<User> users, @NotNull Skill skill) {
        int counter = 0;
        double sum = 0;
        for (User user : users) {
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

    private Integer getUserSkillCount(@NotNull List<User> users, @NotNull Skill skill) {
        int counter = 0;
        for (User user : users) {
            for (UserSkill userSkill : user.getUserSkills()
                    ) {
                if (userSkill.getSkill().equals(skill)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public Integer getTeamSkillCount(@NotNull Team team, @NotNull Skill skill) {
        TeamSkill teamSkill = teamSkillRepository.findTopByTeamAndSkill(team, skill);
        return teamSkill.getSkillCount();
    }

    public Double getTeamAverageSkillLevel(@NotNull Team team, @NotNull Skill skill) {
        TeamSkill teamSkill = teamSkillRepository.findTopByTeamAndSkill(team, skill);
        return teamSkill.getSkillLevelAverage();
    }
}
