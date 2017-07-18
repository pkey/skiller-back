package lt.swedbank.repositories;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface RequestNotificationRepository extends CrudRepository<RequestNotification, Long> {
    Iterable<RequestNotification> findByReceiver(User user);
    Iterable<RequestNotification> findByApprovalRequest(ApprovalRequest approvalRequest);
    Iterable<RequestNotification> findAllByReceiverAndIsNewRequestTrue(User user);
}
