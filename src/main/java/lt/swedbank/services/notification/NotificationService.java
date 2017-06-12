package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private RequestNotificationRepository requestNotificationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ApprovalService approvalService;

    public Iterable<RequestNotification> getNotificationsByUserId(Long id)
    {
        return requestNotificationRepository.findByReceiverAndAnsweredFalse(userService.getUserById(id));
    }

    public RequestNotification  approveByApprovalRequestId(Long id) {
        RequestNotification request = requestNotificationRepository.findOne(id);
        request.setAnswered(true);
        requestNotificationRepository.save(request);
        approvalService.approve(request.getApprovalRequest().getId());
        return request;
    }

    public RequestNotification disapproveByApprovalRequestId(Long id) {
        RequestNotification request = requestNotificationRepository.findOne(id);
        request.setAnswered(true);
        requestNotificationRepository.save(request);
        approvalService.disapprove(request.getApprovalRequest().getId());
        return request;
    }

}
