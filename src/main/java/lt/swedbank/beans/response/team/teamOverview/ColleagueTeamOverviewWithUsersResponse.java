package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.List;

public class ColleagueTeamOverviewWithUsersResponse extends TeamWithUsersResponse {

    public ColleagueTeamOverviewWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, List<TeamSkillTemplateResponse> teamSkillTemplateResponseList) {
        super(team, usersWithSkills, teamSkillTemplateResponseList);
    }

    public ColleagueTeamOverviewWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills) {
        super(team, usersWithSkills);
    }
}
