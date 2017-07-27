package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.List;

public class NonColleagueTeamOverviewWithUsersResponse extends TeamWithUsersResponse {

    public NonColleagueTeamOverviewWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, List<TeamSkillTemplateResponse> teamSkillTemplateResponses) {
        super(team, usersWithSkills, teamSkillTemplateResponses);
    }
}
