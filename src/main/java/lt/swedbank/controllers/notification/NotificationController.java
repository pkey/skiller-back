package lt.swedbank.controllers.notification;


import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.notification.NotificationResponse;
import lt.swedbank.beans.response.notification.RequestNotificationResponse;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.notification.NotificationService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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
    Iterable<NotificationResponse> getNotificationByAuthId(@RequestHeader(value = "Authorization") String authToken) {
        String authId = authenticationService.extractAuthIdFromToken(authToken);
        User user = userService.getUserByAuthId(authId);
        return notificationService.getNotificationResponses(notificationService.getNotificationsByUserId(user.getId()));
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public @ResponseBody
    NotificationResponse approveRequest(@Valid @RequestBody NotificationAnswerRequest notificationAnswerRequest,
                                               @RequestHeader(value = "Authorization") String authToken) {
        User user = userService.getUserByAuthId(authenticationService.extractAuthIdFromToken(authToken));
        return notificationService.handleRequest(notificationAnswerRequest, user);
    }

}


