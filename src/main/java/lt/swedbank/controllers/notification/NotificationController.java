package lt.swedbank.controllers.notification;


import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.notification.ApproverResponse;
import lt.swedbank.beans.response.notification.RequestNotificationResponse;

import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.notification.NotificationService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/notification")
public class NotificationController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<RequestNotificationResponse> getNotificationByAuthId(@RequestHeader(value = "Authorization") String authToken) {
        String authId = authenticationService.extractAuthIdFromToken(authToken);
        User user = userService.getUserByAuthId(authId);

        return notificationService.getRequestNotificationResponse(notificationService.getNotificationsByUserId(user.getId()));
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public @ResponseBody
    RequestNotificationResponse approveRequest(@Valid @RequestBody NotificationAnswerRequest notificationAnswerRequest,
                                               @RequestHeader(value = "Authorization") String authToken) {
        User approver = userService.getUserByAuthId(authenticationService.extractAuthIdFromToken(authToken));
        RequestNotification requestNotification = notificationService.getNotificationById(notificationAnswerRequest.getNotificationId());
        if(notificationAnswerRequest.getApproved() == 1) {
            RequestNotification requestNotification1 = notificationService.approveByApprovalRequestId(notificationAnswerRequest, approver.getId());
            //System.out.println("---" + requestNotification1.getApprovalRequest().getApprovers().get(0).getUser().getName());
            return new RequestNotificationResponse(requestNotification1);
        }
        else if(notificationAnswerRequest.getApproved() == -1) {
            return new RequestNotificationResponse(notificationService.disapproveByApprovalRequestId(notificationAnswerRequest, approver.getId()));
        }
        return new RequestNotificationResponse(notificationService.removeRequestNotification(requestNotification));
    }

}


