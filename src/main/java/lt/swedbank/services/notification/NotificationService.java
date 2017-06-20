package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;
import lt.swedbank.exceptions.notification.NoSuchNotificationException;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<RequestNotificationResponse> getRequestNotificationResponse(Iterable<RequestNotification> requestNotifications) {
        ArrayList<RequestNotificationResponse> requestNotificationResponses = new ArrayList<RequestNotificationResponse>();
        for (RequestNotification requestNotification : requestNotifications ) {
            requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
        }
        return requestNotificationResponses;
    }

    public RequestNotification approveByApprovalRequestId(NotificationAnswerRequest notificationAnswerRequest, Long approversId) {
        RequestNotification requestNotification = getNotificationById(notificationAnswerRequest.getNotificationId());
        ApprovalRequest approvalRequest = approvalService.getApprovalRequestByRequestNotification(requestNotification);
        Integer approves = approvalService.approve(notificationAnswerRequest, approvalRequest, approversId).getApproves();
        if(approves >= 5) {
            Iterable<RequestNotification> requestNotificationList = requestNotificationRepository.findByApprovalRequest(approvalRequest);
            requestNotificationRepository.delete(requestNotificationList);
        } else {
            requestNotificationRepository.delete(requestNotification);
        }
        return requestNotification;
    }

    public RequestNotification disapproveByApprovalRequestId(NotificationAnswerRequest notificationAnswerRequest, Long approversId) {
        RequestNotification requestNotification = getNotificationById(notificationAnswerRequest.getNotificationId());
        ApprovalRequest approvalRequest = approvalService.getApprovalRequestByRequestNotification(requestNotification);
        Iterable<RequestNotification> requestNotificationList = requestNotificationRepository.findByApprovalRequest(approvalRequest);
        String message = notificationAnswerRequest.getMessage();
        approvalService.disapprove(message, requestNotification, approversId);
        requestNotificationRepository.delete(requestNotificationList);
        return requestNotification;
    }

    public RequestNotification getNotificationById(Long id) {
        if(requestNotificationRepository.findOne(id) == null) {
            throw new NoSuchNotificationException();
        }
        return requestNotificationRepository.findOne(id);
    }

    public Iterable<RequestNotification> addNotifications(ApprovalRequest request) {
        return requestNotificationRepository.save(request.getRequestNotifications());
    }

    public void deleteNotifications(ApprovalRequest request) {
        requestNotificationRepository.delete(request.getRequestNotifications());
    }

    public RequestNotification removeRequestNotification(RequestNotification notification) {
        ApprovalRequest approvalRequest = approvalService.getApprovalRequestByRequestNotification(notification);
        approvalRequest.removeNotification(notification);
        approvalService.update(approvalRequest);
        requestNotificationRepository.delete(notification.getId());
        return notification;
    }
}
