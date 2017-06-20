package lt.swedbank.beans.response.user;


import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.response.userSkill.NonColleagueUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.ArrayList;
import java.util.List;

public class NonColleagueResponse extends UserResponse {

    public NonColleagueResponse(User user) {
        super(user);
        skills = convertToUserSkillResponse(user.getUserSkills());
    }

    private List<UserSkillResponse> convertToUserSkillResponse(List<UserSkill> userSkillList){
        List<UserSkillResponse> userSkillResponseList = new ArrayList<>();

        for (UserSkill userSkill:userSkillList
                ) {
            userSkillResponseList.add(new NonColleagueUserSkillResponse(userSkill));
        }

        return userSkillResponseList;
    }

}
