package lt.swedbank.beans.response.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.List;

public class UserWithSkillsAndTeamResponse extends UserWithSkillsResponse {

    protected  String team;
    protected  String division;
    protected  String department;
    protected  String valueStream;

    public UserWithSkillsAndTeamResponse(User user, List<UserSkillResponse> skills) {
        super(user, skills);
        this.team = user.getTeam().getName();
        this.division = user.getTeam().getDepartment().getDivision().getName();
        this.department = user.getDepartment().getName();
        this.valueStream = user.getTeam().getValueStream().getName();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getValueStream() {
        return valueStream;
    }

    public void setValueStream(String valueStream) {
        this.valueStream = valueStream;
    }
}
