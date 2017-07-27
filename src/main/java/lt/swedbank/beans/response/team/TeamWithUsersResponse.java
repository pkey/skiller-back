package lt.swedbank.beans.response.team;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.ArrayList;
import java.util.List;

public class TeamWithUsersResponse extends TeamResponse {

    protected List<UserWithSkillsResponse> users;

    public TeamWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, List<TeamSkillTemplateResponse> teamSkillTemplateResponses) {
        super(team, teamSkillTemplateResponses);
        this.users =  usersWithSkills;
    }

    public TeamWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills) {
        super(team, new ArrayList<>());
        this.users =  usersWithSkills;
    }

    public List<UserWithSkillsResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithSkillsResponse> users) {
        this.users = users;
    }
}
