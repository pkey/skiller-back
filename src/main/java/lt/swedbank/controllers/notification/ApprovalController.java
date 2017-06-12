package lt.swedbank.controllers.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.services.notification.ApprovalService;
import lt.swedbank.services.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @RequestMapping(value = "/approve/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    ApprovalRequest approveRequest(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return approvalService.approveByApprovalRequestId(id);
    }

    @RequestMapping(value = "/disapprove/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    ApprovalRequest disapproveRequest(@RequestHeader(value = "Authorization") String authToken, @PathVariable("id") Long id){
        return approvalService.disapproveByApprovalRequestId(id);
    }

}
