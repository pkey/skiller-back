package lt.swedbank.controllers.notification;


import java.util.ArrayList;
import java.util.List;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;

import lt.swedbank.services.notification.NotificationService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/notification")
public class NotificationController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<RequestNotificationResponse> getNotificationById(@RequestHeader(value = "Authorization") String authToken) {
        User user = userService.getUserByAuthId(authToken);
        return notificationService.getRequestNotificationResponse(notificationService.getNotificationsByUserId(user.getId()));
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public @ResponseBody
    RequestNotificationResponse approveRequest(@RequestHeader(value = "Authorization") String authToken,
                                               @RequestBody NotificationAnswerRequest notificationAnswerRequest) {
        if(notificationAnswerRequest.getApproved())
        {
            return new RequestNotificationResponse(notificationService.approveByApprovalRequestId(notificationAnswerRequest));
        }
        return new RequestNotificationResponse(notificationService.disapproveByApprovalRequestId(notificationAnswerRequest));
    }
}


