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
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public ArrayList<NotificationResponse> getNotificationResponses(Iterable<RequestNotification> requestNotifications) {
        ArrayList<NotificationResponse> requestNotificationResponses = new ArrayList<NotificationResponse>();
        for (RequestNotification requestNotification : requestNotifications) {
            ApprovalRequest approvalRequest = requestNotification.getApprovalRequest();
            if (approvalRequest.getUserSkillLevel().getUserSkill().getUser() == requestNotification.getReceiver()) {
                if (approvalRequest.getStatus() == Status.DISAPPROVED) {
                    requestNotificationResponses.add(new RequestDisapprovedNotificationResponse(requestNotification));
                } else if (approvalRequest.getStatus() == Status.APPROVED) {
                    requestNotificationResponses.add(new RequestApprovedNotificationResponse(requestNotification));
                }
            } else requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
        }
        return requestNotificationResponses;
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

    private RequestNotification changeNotificationRequestStatus(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, NotificationAnswerRequest notificationAnswerRequest) {
        requestNotification.setPending();
        switch (notificationAnswerRequest.getApproved()) {
            case 1:
                requestNotification = approve(approvalRequest, requestNotification, user, notificationAnswerRequest.getMessage());
                break;
            case -1:
                requestNotification = disapprove(approvalRequest, requestNotification, user, notificationAnswerRequest.getMessage());
                break;
            default:
                break;
        }
        return requestNotificationRepository.save(requestNotification);
    }

    public RequestNotification approve(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, String message) {
        requestNotification.setApproved();
        Integer approves = approvalService.approve(message, approvalRequest, user).getApproves();
        if (approves >= 5) {
            setNotificationsAsExpired(approvalRequest.getRequestNotifications());
            sendNotificationAboutSkillLevelStatusChanges(approvalRequest);
        }
        return requestNotification;
    }


    public RequestNotification disapprove(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, String message) {
        requestNotification.setDisapproved();
        approvalService.disapprove(message, approvalRequest, user);
        setNotificationsAsExpired(approvalRequest.getRequestNotifications());
        sendNotificationAboutSkillLevelStatusChanges(approvalRequest);
        return requestNotification;
    }

    public void deleteRequestNotificationsFromApprovalRequest(ApprovalRequest approvalRequest) {
        Iterable<RequestNotification> requestNotificationList = requestNotificationRepository.findByApprovalRequest(approvalRequest);
        requestNotificationRepository.delete(requestNotificationList);
    }


    public void sendNotificationAboutSkillLevelStatusChanges(ApprovalRequest approvalRequest) {
        approvalRequest.setRequestNotification(new RequestNotification(getUserFromApprovalRequest(approvalRequest), approvalRequest));
        approvalService.update(approvalRequest);
    }

    public User getUserFromApprovalRequest(ApprovalRequest approvalRequest) {
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

    private void sortRequestNotifications(List<RequestNotification> requestNotifications) {
        Collections.sort(requestNotifications, Comparator.comparing(RequestNotification::getCreationTime));
    }

    public List<NotificationResponse> getNotificationsResponsesSortedByDate(List<RequestNotification> requestNotifications) {
        sortRequestNotifications(requestNotifications);
        return getNotificationResponses(requestNotifications);
    }
}
