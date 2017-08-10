package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Approver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApproversRepository extends CrudRepository<Approver, Long> {
}
