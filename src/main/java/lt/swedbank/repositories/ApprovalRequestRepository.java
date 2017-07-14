package lt.swedbank.repositories;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkillLevel;
import org.springframework.data.repository.CrudRepository;

public interface ApprovalRequestRepository extends CrudRepository<ApprovalRequest, Long> {
    ApprovalRequest findByUserSkillLevel(UserSkillLevel userSkillLevel);
}
