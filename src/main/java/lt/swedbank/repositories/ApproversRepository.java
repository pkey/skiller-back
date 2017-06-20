package lt.swedbank.repositories;


import lt.swedbank.beans.entity.Approver;
import org.springframework.data.repository.CrudRepository;

public interface ApproversRepository extends CrudRepository<Approver, Long> {
}
