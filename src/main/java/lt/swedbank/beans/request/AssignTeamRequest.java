package lt.swedbank.beans.request;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class AssignTeamRequest {

    @NotNull(message = "User id is required!")
    @ApiModelProperty(required = true, example = "1")
    private Long userId;

    @NotNull(message = "Team id is required!")
    @ApiModelProperty(required = true, example = "1")
    private Long teamId;

    public AssignTeamRequest() {
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
