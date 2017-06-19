package lt.swedbank.beans.response.user;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.Response;
import lt.swedbank.beans.response.serializers.UserTeamSerializer;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.List;

abstract public class UserResponse extends Response {

    protected Long id;

    protected  String name;

    protected  String lastName;

    protected  String email;

    protected List<UserSkillResponse> skills;


    @JsonSerialize(using = UserTeamSerializer.class)
    private Team team;

    public UserResponse(User user) {

        id = user.getId();
        name = user.getName();
        lastName = user.getLastName();
        email = user.getEmail();
        team = user.getTeam();
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


    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<UserSkillResponse> getSkills() {
        return skills;
    }

    public void setSkills(List<UserSkillResponse> skills) {
        this.skills = skills;
    }
}
