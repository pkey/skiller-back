package lt.swedbank.beans.response.user;

import java.util.List;
import lt.swedbank.beans.entity.UserSkill;

public class SearchUserResponse {

    public SearchUserResponse() {}

    private String name;

    private String lastname;

    private List<UserSkill> userSkills;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<UserSkill> getSkillEntityResponseList() {
        return userSkills;
    }

    public void setSkillEntityResponseList(List<UserSkill> userSkills) {
        this.userSkills = userSkills;
    }
}
