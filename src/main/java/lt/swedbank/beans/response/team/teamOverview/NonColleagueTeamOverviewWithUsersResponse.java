package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.List;
import java.util.Set;

public class NonColleagueTeamOverviewWithUsersResponse extends TeamWithUsersResponse {

    public NonColleagueTeamOverviewWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, Set<SkillTemplateResponse> skillTemplateRespons) {
        super(team, usersWithSkills, skillTemplateRespons);
    }
}
