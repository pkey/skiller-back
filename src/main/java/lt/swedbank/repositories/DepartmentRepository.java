package lt.swedbank.repositories;

import lt.swedbank.beans.entity.unit.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
}
