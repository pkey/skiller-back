package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.UserSkillLevel;

public class CurrentSkillLevelResponse {

    private Long id;
    private String title;
    private String status;

    public CurrentSkillLevelResponse(UserSkillLevel userSkillLevel) {
        id = userSkillLevel.getSkillLevel().getId();
        title = userSkillLevel.getSkillLevel().getTitle();
        status = userSkillLevel.getCurrentSkillLevelStatus();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
