package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Approval;
import org.springframework.data.repository.CrudRepository;

public interface ApprovalRepository extends CrudRepository<Approval, Long> {
}
