package lt.swedbank.beans.response.team;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamWithUsersResponse extends TeamResponse {

    protected List<UserWithSkillsResponse> users;

    public TeamWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, Set<SkillTemplateResponse> skillTemplateResponses) {
        super(team, skillTemplateResponses);
        this.users =  usersWithSkills;
    }

    public TeamWithUsersResponse(Team team, List<UserWithSkillsResponse> usersWithSkills) {
        super(team, new HashSet<>());
        this.users =  usersWithSkills;
    }

    public List<UserWithSkillsResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithSkillsResponse> users) {
        this.users = users;
    }
}
