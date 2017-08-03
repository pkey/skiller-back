package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.List;

public class ColleagueTeamOverviewWithUsersResponse extends TeamWithUsersResponse {

    public ColleagueTeamOverviewWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, List<SkillTemplateResponse> skillTemplateResponseList) {
        super(team, usersWithSkills, skillTemplateResponseList);
    }

    public ColleagueTeamOverviewWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills) {
        super(team, usersWithSkills);
    }
}
