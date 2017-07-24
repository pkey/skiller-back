package lt.swedbank.beans.response.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.response.userSkill.NormalUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class UserEntityResponse extends UserResponse {


    public UserEntityResponse(User user) {
        super(user);
        skills = convertToUserSkillResponse(user.getUserSkills());
    }


    private List<UserSkillResponse> convertToUserSkillResponse(List<UserSkill> userSkillList){
        List<UserSkillResponse> userSkillResponseList = new ArrayList<>();

        Collections.sort(userSkillList);
        Collections.reverse(userSkillList);
        
        for (UserSkill userSkill: userSkillList
             ) {
            userSkillResponseList.add(new NormalUserSkillResponse(userSkill));
        }

        return userSkillResponseList;
    }
}

