package lt.swedbank.beans.request;

import lt.swedbank.beans.entity.Skill;

import javax.validation.constraints.NotNull;

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