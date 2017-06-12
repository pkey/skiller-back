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
    private NotificationService notificationService;
    @Autowired
    private UserService userService;


    public ApprovalRequest approve(Long approvalRequestId, Long approverId) {
        ApprovalRequest request = approvalRepository.findOne(approvalRequestId);
        request.approve();
        if(request.getApproves() >= 5)
        {
            request.approve();
            request.addApprover(userService.getUserById(approverId));
            deleteNotifications(request);
        }
        approvalRepository.save(request);
        return request;
    }

    private void deleteNotifications(ApprovalRequest request) {
        notificationService.deleteNotifications(request);
    }

    public ApprovalRequest disapprove(Long approvalRequestId, Long approverId) {

        ApprovalRequest request = approvalRepository.findOne(approvalRequestId);
        request.disapprove();
        request.setDisapprover(userService.getUserById(approverId));
        approvalRepository.save(request);
        return request;
    }

}
