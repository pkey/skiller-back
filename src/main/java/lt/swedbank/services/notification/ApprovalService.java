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


    public ApprovalRequest approve(Long id) {
        ApprovalRequest request = approvalRepository.findOne(id);
        request.approve();
        if(request.getApproves() >= 5)
        {
            disableNotifications(request);
        }
        approvalRepository.save(request);
        return request;
    }

    public ApprovalRequest disapprove(Long id) {

        ApprovalRequest request = approvalRepository.findOne(id);
        request.disapprove();
        disableNotifications(request);
        approvalRepository.save(request);
        return request;
    }

    public ApprovalRequest disableNotifications(ApprovalRequest approvalRequest)
    {
        for (RequestNotification notification: approvalRequest.getRequestNotification()
             ) {
            notification.setAnswered(true);
        }
        return approvalRequest;
    }

}
