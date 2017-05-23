package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.Department;

public class DepartmentsListResponse extends Response {

    private Iterable<Department> departments;

    public DepartmentsListResponse(Iterable<Department> departments) {
        this.departments = departments;
    }

    public Iterable<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Iterable<Department> departments) {
        this.departments = departments;
    }
}
