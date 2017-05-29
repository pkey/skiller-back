package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Iterable<DepartmentEntityResponse> getAllDepartments() {
        List<DepartmentEntityResponse> departmentList = new ArrayList<>();
        for (Department department: departmentRepository.findAll()
                ) {
            departmentList.add(new DepartmentEntityResponse(department));
        }
        return departmentList;
    }
}
