package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.UserSkillLevel;

public class CurrentSkillLevelResponse {

    private Long id;
    private String title;


    public CurrentSkillLevelResponse(UserSkillLevel skillLevel) {
        id = skillLevel.getSkillLevel().getId();
        title = skillLevel.getSkillLevel().getTitle();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
