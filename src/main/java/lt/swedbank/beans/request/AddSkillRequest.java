package lt.swedbank.beans.request;

import lt.swedbank.beans.Skill;
import lt.swedbank.beans.User;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddSkillRequest {

    @NotNull(message = "Skill title is required!")
    private String title;


    public AddSkillRequest() {}

    public AddSkillRequest(Skill skill) {
        setTitle(skill.getTitle());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
