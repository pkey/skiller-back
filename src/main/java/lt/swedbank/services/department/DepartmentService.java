package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.ValueStream;
import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.exceptions.department.DepartmentNotFoundException;
import lt.swedbank.exceptions.valueStream.ValueStreamNotFoundException;
import lt.swedbank.repositories.DepartmentRepository;
import lt.swedbank.repositories.ValueStreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ValueStreamRepository valueStreamRepository;

    public Department getDepartmentById(@NotNull Long id) {
        Department department = departmentRepository.findOne(id);
        if (department == null) {
            throw new DepartmentNotFoundException();
        }
        return department;
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

    public ValueStream getValueStreamById(Long id) {
        ValueStream valueStream = valueStreamRepository.findOne(id);
        if (valueStream == null) {
            throw new ValueStreamNotFoundException();
        }
        return valueStream;
    }
}
