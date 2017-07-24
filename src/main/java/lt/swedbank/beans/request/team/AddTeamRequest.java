package lt.swedbank.beans.request.team;

import javax.validation.constraints.NotNull;
import java.util.List;

public class AddTeamRequest {
    @NotNull(message = "Team title is required")
    private String name;

    @NotNull(message = "Department id is required")
    private Long departmentId;

    @NotNull(message = "User ids must be sent")
    private List<Long> userIds;
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
}
