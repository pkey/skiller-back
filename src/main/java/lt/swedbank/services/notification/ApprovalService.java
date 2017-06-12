package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.repositories.ApprovalRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;
    @Autowired
    private UserService userService;

    public Iterable<ApprovalRequest> getApprovalRequestsByUserId(Long id)
    {
        return null;
    }
    public ApprovalRequest approveByApprovalRequestId(Long id) {
        ApprovalRequest request = approvalRepository.findOne(id);
        request.approve();
        approvalRepository.save(request);
        return request;
    }

    public ApprovalRequest disapproveByApprovalRequestId(Long id) {
        ApprovalRequest request = approvalRepository.findOne(id);
        request.disapprove();
        approvalRepository.save(request);
        return request;
    }
}
