package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.List;

public class ColleagueTeamOverviewResponse extends TeamResponse {

    public ColleagueTeamOverviewResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, List<TeamSkillTemplateResponse> teamSkillTemplateResponseList) {
        super(team, usersWithSkills, teamSkillTemplateResponseList);
    }

    public ColleagueTeamOverviewResponse(Team team, List<UserWithSkillsResponse> usersWithSkills) {
        super(team, usersWithSkills);
    }
}
