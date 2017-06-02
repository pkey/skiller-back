package lt.swedbank.beans.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class AssignSkillLevelRequest {

    @NotNull(message = "Level ID is required")
    @ApiModelProperty(required = true, example = "1")
    private Long levelId;

    @NotNull(message = "Skill ID is required")
    @ApiModelProperty(required = true, example = "1")
    private Long skillId;

    @NotNull(message = "Description is required")
    @ApiModelProperty(required = true, example = "I am upgrading, because I am too cool")
    private String motivation;

    public AssignSkillLevelRequest() {
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

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }
}
