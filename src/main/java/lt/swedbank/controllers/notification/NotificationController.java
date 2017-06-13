package lt.swedbank.controllers.notification;


import java.util.ArrayList;
import java.util.List;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;

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

        if(notificationAnswerRequest.getApproved().toLowerCase().equals("true"))
        {
            return new RequestNotificationResponse(notificationService.approveByApprovalRequestId(notificationAnswerRequest));
        }
        return new RequestNotificationResponse(notificationService.disapproveByApprovalRequestId(notificationAnswerRequest));
    }
}


