package lt.swedbank.repositories;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.ApprovalRequestAnswerers;
import org.springframework.data.repository.CrudRepository;

public interface ApprovalRequestAnswerersRepository extends CrudRepository<ApprovalRequestAnswerers, Long> {
    Iterable<ApprovalRequestAnswerers> findByApprovalRequestAndApprovedTrue(ApprovalRequest request);
}
