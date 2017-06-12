package lt.swedbank.controllers.notification;


import java.util.List;
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

    @RequestMapping(value = "/approve/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    RequestNotificationResponse approveRequest(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return new RequestNotificationResponse(notificationService.approveByApprovalRequestId(id));
    }

    @RequestMapping(value = "/disapprove/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    RequestNotificationResponse disapproveRequest(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return new RequestNotificationResponse(notificationService.disapproveByApprovalRequestId(id));
    }

}


