package lt.swedbank.beans.request.team;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AddTeamRequest {
    @NotNull(message = "Team name is required")
    private String name;

    @NotNull(message = "Department id is required")
    private Long departmentId;

    @NotNull
    private List<Long> userIds;

    @NotNull
    private List<Long> skillIds;

    private Long streamId;

    public AddTeamRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Long> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<Long> skillIds) {
        this.skillIds = skillIds;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }
}
