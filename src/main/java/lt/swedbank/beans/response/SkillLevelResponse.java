package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.SkillLevel;

public class SkillLevelResponse {

    private Long id;
    private Long level;
    private String title;
    private String description;

    public SkillLevelResponse(SkillLevel skillLevel) {
        this.id = skillLevel.getId();
        this.level = skillLevel.getLevel();
        this.title = skillLevel.getTitle();
        this.description = skillLevel.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
