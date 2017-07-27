package lt.swedbank.beans.response.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.userSkill.NonColleagueUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.List;

public class NonColleagueWithTeamResponse extends NonColleagueResponse {

    protected  String team;
    protected  String division;
    protected  String department;
    protected  String valueStream;

    public NonColleagueWithTeamResponse(User user, List<NonColleagueUserSkillResponse> skills) {
        super(user, skills);
        this.team = user.getTeam().getName();
        this.division = user.getTeam().getDepartment().getDivision().getName();
        this.department = user.getDepartment().getName();
        this.valueStream = user.getTeam().getValueStream().getName();
    }

}
