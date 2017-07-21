package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.division.DivisionResponse;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.user.UserResponse;

import java.util.List;

public abstract class TeamOverviewResponse extends TeamResponse {

    protected List<UserResponse> users;

    public TeamOverviewResponse(Team team) {
        super(team);
    }

    public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }

}
