package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.notification.NotificationResponse;
import lt.swedbank.beans.response.notification.RequestApprovedNotificationResponse;
import lt.swedbank.beans.response.notification.RequestDisapprovedNotificationResponse;
import lt.swedbank.beans.response.notification.RequestNotificationResponse;
import lt.swedbank.exceptions.notification.NoSuchNotificationException;
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

    public Iterable<RequestNotification> getNotificationsByUser(User user) {
        return requestNotificationRepository.findByReceiver(user);
    }

    public ArrayList<NotificationResponse> getNotificationResponses(Iterable<RequestNotification> requestNotifications) {
        ArrayList<NotificationResponse> requestNotificationResponses = new ArrayList<NotificationResponse>();
        for (RequestNotification requestNotification : requestNotifications) {
            ApprovalRequest approvalRequest = requestNotification.getApprovalRequest();
            if (approvalRequest.isApproved() == -1) {
                requestNotificationResponses.add(new RequestDisapprovedNotificationResponse(requestNotification));
            } else if (approvalRequest.isApproved() == 0) {
                requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
            } else if (approvalRequest.isApproved() == 1) {
                requestNotificationResponses.add(new RequestApprovedNotificationResponse(requestNotification));
            }
        }
        return requestNotificationResponses;
    }

    public NotificationResponse handleRequest(NotificationAnswerRequest notificationAnswerRequest , User user) {

        RequestNotification requestNotification = getNotificationById(notificationAnswerRequest.getNotificationId());
        ApprovalRequest approvalRequest = approvalService.getApprovalRequestByRequestNotification(requestNotification);

        requestNotification.setNewNotification(false);

        if(notificationAnswerRequest.getApproved() == 1) {
            return new RequestNotificationResponse(approve(approvalRequest, requestNotification, user.getId(), notificationAnswerRequest.getMessage()));
        }
        else if(notificationAnswerRequest.getApproved() == -1) {
            return new RequestNotificationResponse(disapprove(approvalRequest, requestNotification, user.getId(), notificationAnswerRequest.getMessage()));
        }
        return new RequestNotificationResponse(removeRequestNotification(approvalRequest, requestNotification));
    }

    public RequestNotification approve(ApprovalRequest approvalRequest, RequestNotification requestNotification,Long approversId, String message) {

        Integer approves = approvalService.approve(message, approvalRequest, approversId).getApproves();
        if (approves >= 5) {
            requestNotification.setApproved();
            sendRequestNotifications(approvalRequest);
        }
        return requestNotification;
    }

    public RequestNotification disapprove(ApprovalRequest approvalRequest, RequestNotification requestNotification, Long approversId, String message) {

        approvalService.disapprove(message, requestNotification, approversId);
        requestNotification.setDisapproved();
        sendRequestNotifications(approvalRequest);
        return requestNotification;
    }

    public void deleteRequestNotificationsFromApprovalRequest(ApprovalRequest approvalRequest)
    {
        Iterable<RequestNotification> requestNotificationList = requestNotificationRepository.findByApprovalRequest(approvalRequest);
        requestNotificationRepository.delete(requestNotificationList);
    }


    public void sendRequestNotifications(ApprovalRequest approvalRequest)
    {
        approvalRequest.setRequestNotification(new RequestNotification(getUserFromApprovalRequest(approvalRequest), approvalRequest));
        approvalService.update(approvalRequest);
    }

    public User getUserFromApprovalRequest(ApprovalRequest approvalRequest)
    {
        return userService.getUserById(approvalRequest.getUserSkillLevel().getUserSkill().getUser().getId());
    }

    public RequestNotification getNotificationById(Long id) {
        if (requestNotificationRepository.findOne(id) == null) {
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

    public RequestNotification removeRequestNotification(ApprovalRequest approvalRequest, RequestNotification notification) {
        notification.setNewNotification(false);

        approvalService.update(approvalRequest);
        return notification;
    }

}
