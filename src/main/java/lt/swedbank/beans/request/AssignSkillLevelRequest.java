package lt.swedbank.beans.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
public class AssignSkillLevelRequest {

    @NotNull(message = "Level ID is required")
    @NonNull
    @ApiModelProperty(required = true, example = "1")
    private Long levelId;

    @NotNull(message = "Skill ID is required")
    @NonNull
    @ApiModelProperty(required = true, example = "1")
    private Long skillId;

    @NotNull(message = "Description is required")
    @NonNull
    @ApiModelProperty(required = true, example = "I am upgrading, because I am too cool")
    private String motivation;

}
