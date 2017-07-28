package lt.swedbank.beans.response.department;


import lt.swedbank.beans.entity.Department;

import javax.validation.constraints.NotNull;

public class DepartmentResponse {

    private Long id;
    private String name;

    public DepartmentResponse() {
    }

    public DepartmentResponse(@NotNull Department department) {
        this.id = department.getId();
        this.name = department.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
