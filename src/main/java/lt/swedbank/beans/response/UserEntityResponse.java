package lt.swedbank.beans.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.response.serializers.UserSkillsSerializer;
import lt.swedbank.beans.response.serializers.UserTeamSerializer;

import java.util.List;


public class UserEntityResponse extends Response {

    private Long id;

    private String name;

    private String lastName;

    private String email;


    @JsonSerialize(using = UserSkillsSerializer.class)
    private List<UserSkill> userSkills;

    @JsonSerialize(using = UserTeamSerializer.class)
    private Team team;

    public UserEntityResponse(User user) {

        id = user.getId();
        name = user.getName();
        lastName = user.getLastName();
        email = user.getEmail();
        userSkills = user.getUserSkills();
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

    public List<UserSkill> getUserSkills() {
        return userSkills;
    }

    public void setUserSkills(List<UserSkill> userSkills) {
        this.userSkills = userSkills;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
