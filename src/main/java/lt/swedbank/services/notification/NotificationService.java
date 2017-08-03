package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.enums.Status;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.notification.NotificationResponse;
import lt.swedbank.beans.response.notification.RequestApprovedNotificationResponse;
import lt.swedbank.beans.response.notification.RequestDisapprovedNotificationResponse;
import lt.swedbank.beans.response.notification.RequestNotificationResponse;
import lt.swedbank.exceptions.notification.NoSuchNotificationException;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NotificationService {

    @Autowired
    private RequestNotificationRepository requestNotificationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ApprovalService approvalService;

    @Value("${notification.approves_needed}")
    public  Integer APPROVES_NEEDED;
    @Value("${notification.disapproves_needed}")
    public  Integer DISAPPROVES_NEEDED;

    public Iterable<RequestNotification> getNotificationsByUser(User user) {
        return requestNotificationRepository.findByReceiver(user);
    }

    public Iterable<RequestNotification> setNotificationsAsExpired(Iterable<RequestNotification> requestNotifications) {
        for (RequestNotification requestNotification : requestNotifications) {
            requestNotification.setExpired();
        }
        requestNotificationRepository.save(requestNotifications);
        return requestNotifications;
    }

    public NotificationResponse handleRequest(NotificationAnswerRequest notificationAnswerRequest, User user) {
        RequestNotification requestNotification = getNotificationById(notificationAnswerRequest.getNotificationId());
        ApprovalRequest approvalRequest = approvalService.getApprovalRequestByRequestNotification(requestNotification);
        switch (approvalRequest.getStatus()) {
            case PENDING:
                requestNotification = changeNotificationRequestStatus(approvalRequest, requestNotification, user, notificationAnswerRequest);
                break;
            case APPROVED:
                requestNotification.setExpired();
                requestNotificationRepository.save(requestNotification);
                return new RequestApprovedNotificationResponse(requestNotification);
            case DISAPPROVED:
                requestNotification.setExpired();
                requestNotificationRepository.save(requestNotification);
                return new RequestDisapprovedNotificationResponse(requestNotification);
        }
        return new RequestNotificationResponse(requestNotification);
    }

    public ArrayList<NotificationResponse> getNotificationResponses(Iterable<RequestNotification> requestNotifications) {
        ArrayList<NotificationResponse> requestNotificationResponses = new ArrayList<NotificationResponse>();
        for (RequestNotification requestNotification : requestNotifications) {
            ApprovalRequest approvalRequest = requestNotification.getApprovalRequest();
            if (approvalRequest.getUserSkillLevel().getUserSkill().getUser().equals(requestNotification.getReceiver())) {
                if (approvalRequest.getStatus() == Status.DISAPPROVED) {
                    requestNotificationResponses.add(new RequestDisapprovedNotificationResponse(requestNotification));
                } else if (approvalRequest.getStatus() == Status.APPROVED) {
                    requestNotificationResponses.add(new RequestApprovedNotificationResponse(requestNotification));
                }
            } else requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
        }
        return requestNotificationResponses;
    }

    public RequestNotification getNotificationById(Long id) {
        if (requestNotificationRepository.findOne(id) == null) {
            throw new NoSuchNotificationException();
        }
        return requestNotificationRepository.findOne(id);
    }

    private RequestNotification changeNotificationRequestStatus(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, NotificationAnswerRequest notificationAnswerRequest) {
        requestNotification.setPending();
        switch (notificationAnswerRequest.getStatus()) {
            case APPROVED:
                requestNotification = approve(approvalRequest, requestNotification, user, notificationAnswerRequest.getMessage());
                break;
            case DISAPPROVED:
                requestNotification = disapprove(approvalRequest, requestNotification, user, notificationAnswerRequest.getMessage());
                break;
            case PENDING:
                requestNotification = setNotificationsAsPending(approvalRequest, requestNotification, user);
            default:
                break;
        }
        return requestNotificationRepository.save(requestNotification);
    }

    private RequestNotification approve(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, String message) {
        requestNotification.setApproved();
        Integer approves = approvalService.approve(message, approvalRequest, user).getApprovers().size();
        if (approves >= APPROVES_NEEDED) {
            setNotificationsAsExpired(approvalRequest.getRequestNotifications());
            sendNotificationAboutSkillLevelStatusChanges(approvalRequest);
        }
        return requestNotification;
    }

    private RequestNotification disapprove(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, String message) {
        requestNotification.setDisapproved();
        approvalService.disapprove(message, approvalRequest, user);
        Integer disapproves = approvalService.disapprove(message, approvalRequest, user).getDisapprovers().size();
        if (disapproves >= DISAPPROVES_NEEDED ) {
            setNotificationsAsExpired(approvalRequest.getRequestNotifications());
            sendNotificationAboutSkillLevelStatusChanges(approvalRequest);
        }
        return requestNotification;
    }

    private RequestNotification setNotificationsAsPending(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user) {
        approvalService.removeUserFromApproversAndDisapproversIfExists(approvalRequest, user);
        requestNotification.setPending();
        return requestNotification;
    }

    private User getUserFromApprovalRequest(ApprovalRequest approvalRequest) {
        return userService.getUserById(approvalRequest.getUserSkillLevel().getUserSkill().getUser().getId());
    }

    private void sendNotificationAboutSkillLevelStatusChanges(ApprovalRequest approvalRequest) {
        approvalRequest.setRequestNotification(new RequestNotification(getUserFromApprovalRequest(approvalRequest), approvalRequest));
        approvalService.update(approvalRequest);
    }
}
