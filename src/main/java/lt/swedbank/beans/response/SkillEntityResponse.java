package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.Skill;

public class SkillEntityResponse extends Response {

    private String title;
    private Long id;


    public SkillEntityResponse(Skill skill) {
        this.id = skill.getId();
        this.title = skill.getTitle();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
