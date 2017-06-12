package lt.swedbank.controllers.notification;


import java.util.ArrayList;
import java.util.List;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
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

    @RequestMapping(value = "/get/test/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<RequestNotificationResponse> test(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id) {

        List<RequestNotificationResponse> requestNotificationResponses = new ArrayList<>();

        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setId(Long.parseLong("2"));
        approvalRequest.setMessage("Vilius67 is a Be(a)st!");
        RequestNotification requestNotification = new RequestNotification();
        requestNotification.setApprovalRequest(approvalRequest);
        requestNotification.setReceiver(new User());

        ApprovalRequest approvalRequest2 = new ApprovalRequest();
        approvalRequest2.setId(Long.parseLong("3"));
        approvalRequest2.setMessage("Mantelis expert in modal making!");
        RequestNotification requestNotification2 = new RequestNotification();
        requestNotification.setApprovalRequest(approvalRequest2);
        requestNotification.setReceiver(new User());


        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification2));
        return requestNotificationResponses;
    }
}


