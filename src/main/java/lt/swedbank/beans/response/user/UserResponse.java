package lt.swedbank.beans.response.user;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.Response;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.List;
import java.util.Optional;

public class UserResponse extends Response{

    protected Long id;

    protected  String name;

    protected  String lastName;

    protected  String email;

    protected List<? extends UserSkillResponse> skills;

    protected TeamResponse team;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.team = user.getTeam() == null ? null : new TeamResponse(user.getTeam());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<? extends UserSkillResponse> getSkills() {
        return skills;
    }

    public void setSkills(List<? extends UserSkillResponse> skills) {
        this.skills = skills;
    }

    public TeamResponse getTeam() {
        return team;
    }
}
