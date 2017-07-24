package lt.swedbank.beans.request;

import javax.validation.constraints.NotNull;

public class AddTeamRequest {
    @NotNull(message = "Team title is required")
    private String name;

    @NotNull(message = "Department id is required")
    private Long departmentId;

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
}
