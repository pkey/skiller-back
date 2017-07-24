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

    public Iterable<RequestNotification> setNotificationsAsExpired(Iterable<RequestNotification> requestNotifications){
        for (RequestNotification requestNotification: requestNotifications) {
            requestNotification.setExpired();
        }
        requestNotificationRepository.save(requestNotifications);
        return requestNotifications;
    }

    public ArrayList<NotificationResponse> getNotificationResponses(Iterable<RequestNotification> requestNotifications) {



        ArrayList<NotificationResponse> requestNotificationResponses = new ArrayList<NotificationResponse>();
        for (RequestNotification requestNotification : requestNotifications) {

            ApprovalRequest approvalRequest = requestNotification.getApprovalRequest();
            if(approvalRequest.getUserSkillLevel().getUserSkill().getUser() == requestNotification.getReceiver()) {
                if (approvalRequest.isApproved() == -1) {
                    requestNotificationResponses.add(new RequestDisapprovedNotificationResponse(requestNotification));
                }
                else if (approvalRequest.isApproved() == 1) {
                    requestNotificationResponses.add(new RequestApprovedNotificationResponse(requestNotification));
                }
            }
            else requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
        }


        return requestNotificationResponses;
    }

    public NotificationResponse handleRequest(NotificationAnswerRequest notificationAnswerRequest , User user) {

        RequestNotification requestNotification = getNotificationById(notificationAnswerRequest.getNotificationId());
        ApprovalRequest approvalRequest = approvalService.getApprovalRequestByRequestNotification(requestNotification);

        if (approvalRequest.isApproved() == 0) {

            changeNotificationRequestStatus(requestNotification, notificationAnswerRequest.getApproved());
            requestNotification.setNewNotification(false);

            if (notificationAnswerRequest.getApproved() == 0) {
                requestNotification =  approve(approvalRequest, requestNotification, user, notificationAnswerRequest.getMessage());
            } else if (notificationAnswerRequest.getApproved() == -1) {
                requestNotification = disapprove(approvalRequest, requestNotification, user, notificationAnswerRequest.getMessage());
            }
            removeRequestNotification(approvalRequest, requestNotification);
        }
        if(notificationAnswerRequest.getApproved() == 1) {
            return new RequestApprovedNotificationResponse(requestNotification);
        } else  if(notificationAnswerRequest.getApproved() == -1) {
            return new RequestDisapprovedNotificationResponse(requestNotification);
        }

        return new RequestNotificationResponse(requestNotification);
    }

    private void changeNotificationRequestStatus(RequestNotification requestNotification, Integer status) {

        if(status == 1) {
            requestNotification.setApproved();
        }
        else if( status == -1) {
            requestNotification.setDisapproved();
        }
        requestNotificationRepository.save(requestNotification);
    }

    public RequestNotification approve(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, String message) {

        Integer approves = approvalService.approve(message, approvalRequest, user).getApproves();
        if (approves >= 5) {
            requestNotification.setApproved();
            sendNotificationAboutSkillLevelStatusChanges(approvalRequest);
        }
        return requestNotification;
    }

    public RequestNotification disapprove(ApprovalRequest approvalRequest, RequestNotification requestNotification, User user, String message) {

        approvalService.disapprove(message, approvalRequest, user);
        sendNotificationAboutSkillLevelStatusChanges(approvalRequest);
        return requestNotification;
    }

    public void deleteRequestNotificationsFromApprovalRequest(ApprovalRequest approvalRequest)
    {
        Iterable<RequestNotification> requestNotificationList = requestNotificationRepository.findByApprovalRequest(approvalRequest);
        requestNotificationRepository.delete(requestNotificationList);
    }


    public void sendNotificationAboutSkillLevelStatusChanges(ApprovalRequest approvalRequest)
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

        approvalService.update(approvalRequest);
        return notification;
    }

}
