package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.List;

public class NonColleagueTeamOverviewResponse extends TeamResponse {

    public NonColleagueTeamOverviewResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, List<TeamSkillTemplateResponse> teamSkillTemplateResponses) {
        super(team, usersWithSkills, teamSkillTemplateResponses);
    }
}
