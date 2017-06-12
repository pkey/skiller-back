package lt.swedbank.controllers.notification;


import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.response.Response;
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
    Iterable<RequestNotification> getNotificationById(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return notificationService.getNotificationsByUserId(id);
    }

    @RequestMapping(value = "/approve/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    RequestNotification approveRequest(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return notificationService.approveByApprovalRequestId(id);
    }

    @RequestMapping(value = "/disapprove/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    RequestNotification disapproveRequest(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return notificationService.disapproveByApprovalRequestId(id);
    }

}


