package lt.swedbank.beans.response;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class AssignSkillLevelResponse extends Response{

    private Long levelId;

    private Long skillId;

    private String description;

    public AssignSkillLevelResponse() {
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
