package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.repositories.DepartmentRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

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
}
