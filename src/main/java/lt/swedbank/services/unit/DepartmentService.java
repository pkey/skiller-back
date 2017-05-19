package lt.swedbank.services.unit;

import lt.swedbank.beans.entity.unit.Department;
import lt.swedbank.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Iterable<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

}
