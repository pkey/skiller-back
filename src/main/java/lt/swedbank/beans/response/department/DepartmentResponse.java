package lt.swedbank.beans.response.department;


import lombok.Data;
import lombok.NonNull;
import lt.swedbank.beans.entity.Department;

import javax.validation.constraints.NotNull;

@Data
public class DepartmentResponse {

    @NonNull
    private Long id;
    @NonNull
    private String name;

    public DepartmentResponse(@NotNull Department department) {
        this.id = department.getId();
        this.name = department.getName();
    }

}
