package lt.swedbank.repositories;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface ApprovalRepository extends CrudRepository<ApprovalRequest, Long> {
}
