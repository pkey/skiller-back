package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.department.DepartmentEntityResponse;
import lt.swedbank.beans.response.department.DepartmentOverviewResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.exceptions.department.DepartmentNotFoundException;
import lt.swedbank.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department getDepartmentById(@NotNull Long id) {
        Department department = departmentRepository.findOne(id);
        if (department == null) {
            throw new DepartmentNotFoundException();
        }
        return department;
    }

    public DepartmentOverviewResponse getDepartmentOverviewByDepartmentId(@NotNull Long id) {
        Department department = this.getDepartmentById(id);

        return new DepartmentOverviewResponse(new DepartmentResponse(department),
                this.getUsersFromDepartment(department).stream().map(UserResponse::new).collect(Collectors.toList()),
                this.;


    }
    public Iterable<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Iterable<DepartmentEntityResponse> getAllDepartmentEntityResponseList() {
        List<DepartmentEntityResponse> departmentList = new ArrayList<>();
        for (Department department : getAllDepartments()
                ) {
            departmentList.add(new DepartmentEntityResponse(department));
        }
        return departmentList;
    }

    private List<User> getUsersFromDepartment(Department department) {
        List<User> users = new ArrayList<>();
        department.getTeams().forEach(team -> users.addAll(team.getUsers()));
        return users;
    }
}
