package lt.swedbank.beans.response.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.response.Response;
import lt.swedbank.beans.response.userSkill.NormalUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserWithoutTeamResponse  extends Response {

        protected Long id;

        protected  String name;

        protected  String lastName;

        protected  String email;

        protected List<UserSkillResponse> skills;


        public UserWithoutTeamResponse(User user) {
            id = user.getId();
            name = user.getName();
            lastName = user.getLastName();
            email = user.getEmail();
            skills = convertToUserSkillResponse(user.getUserSkills());

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


        public List<UserSkillResponse> getSkills() {
            return skills;
        }

        public void setSkills(List<UserSkillResponse> skills) {
            this.skills = skills;
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

