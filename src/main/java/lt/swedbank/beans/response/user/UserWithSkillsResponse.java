package lt.swedbank.beans.response.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.List;


public class UserWithSkillsResponse extends UserResponse {


    public UserWithSkillsResponse(User user, List<UserSkillResponse> skills) {
        super(user);
        this.setSkills(skills);
    }
}

