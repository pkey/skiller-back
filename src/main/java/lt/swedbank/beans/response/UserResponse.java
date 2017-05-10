package lt.swedbank.beans.response;

import lt.swedbank.beans.Skill;
import lt.swedbank.beans.User;

import java.util.List;

public class UserResponse extends Response{

    private String name;

    private String lastName;

    private String email;

    private List<Skill> skills;

    public UserResponse(User user) {

        name = user.getName();
        lastName = user.getLastName();
        email = user.getEmail();
        skills = user.getSkills();

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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
