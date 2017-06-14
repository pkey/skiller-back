package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NotificationService {

    @Autowired
    private RequestNotificationRepository requestNotificationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSkillService userSkillService;
    @Autowired
    private ApprovalService approvalService;

    public Iterable<RequestNotification> getNotificationsByUserId(Long id)
    {
        return requestNotificationRepository.findByReceiver(userService.getUserById(id));
    }

    public ArrayList<RequestNotificationResponse> getRequestNotificationResponse(Iterable<RequestNotification> requestNotifications)
    {
        ArrayList<RequestNotificationResponse> requestNotificationResponses = new ArrayList<RequestNotificationResponse>();
        for (RequestNotification requestNotification : requestNotifications ) {
            requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
        }
        return requestNotificationResponses;
    }

    public RequestNotification approveByApprovalRequestId(NotificationAnswerRequest notificationAnswerRequest) {
        RequestNotification request = requestNotificationRepository.findOne(notificationAnswerRequest.getNotificationId());
        requestNotificationRepository.delete(request);
        approvalService.approve(request.getApprovalRequest().getId(), notificationAnswerRequest.getApproverId());
        return request;
    }

    public RequestNotification disapproveByApprovalRequestId(NotificationAnswerRequest notificationAnswerRequest) {
        RequestNotification request = requestNotificationRepository.findOne(notificationAnswerRequest.getNotificationId());
        requestNotificationRepository.delete(request);
        approvalService.disapprove(request.getApprovalRequest().getId(), notificationAnswerRequest.getApproverId());
        return request;
    }

    //TODO should return something to know that notifications were saved successfully
    public void addNotifications(ApprovalRequest request) {
        requestNotificationRepository.save(request.getRequestNotifications());
    }

    public void deleteNotifications(ApprovalRequest request) {
        requestNotificationRepository.delete(request.getRequestNotifications());
    }
}
