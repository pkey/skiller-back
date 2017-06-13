package lt.swedbank.controllers.notification;


import java.util.ArrayList;
import java.util.List;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;

import lt.swedbank.services.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/all/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<RequestNotificationResponse> getNotificationById(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
         return notificationService.getRequestNotificationResponse(notificationService.getNotificationsByUserId(id));
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


