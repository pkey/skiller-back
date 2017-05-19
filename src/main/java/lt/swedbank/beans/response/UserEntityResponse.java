package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.User;

import java.util.List;

public class UserEntityResponse extends Response implements Comparable<UserEntityResponse> {

    private String name;

    private String lastName;

    private String email;

    private List<UserSkill> userSkills;

    public UserEntityResponse(User user) {

        name = user.getName();
        lastName = user.getLastName();
        email = user.getEmail();
        userSkills = user.getUserSkills();

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

    public String getFullName()
    {
        return this.name + " " + this.lastName;
    }

    @Override
    public int compareTo(UserEntityResponse userEntityResponse) {
        return this.getFullName().compareTo(userEntityResponse.getFullName());
    }
}
