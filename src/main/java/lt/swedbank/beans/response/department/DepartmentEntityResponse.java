package lt.swedbank.beans.response.department;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.response.Response;
import lt.swedbank.beans.response.team.TeamResponse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class DepartmentEntityResponse extends Response {

    @NonNull
    private DepartmentResponse departmentResponse;
    @NonNull
    private List<TeamResponse> teams;

    public DepartmentEntityResponse(Department department) {
        this.departmentResponse = new DepartmentResponse(department);
        this.teams = department.getTeams().stream().map(TeamResponse::new).collect(Collectors.toList());
    }
}