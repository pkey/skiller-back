package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.response.DepartmentsListResponse;
import lt.swedbank.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public DepartmentsListResponse getAllDepartments() {
        return new DepartmentsListResponse(departmentRepository.findAll());
    }

}
