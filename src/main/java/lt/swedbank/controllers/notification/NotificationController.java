package lt.swedbank.controllers.notification;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;

import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.repositories.UserSkillRepository;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.notification.NotificationService;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.skill.UserSkillService;
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
        if(notificationAnswerRequest.getApproved() == 1)
        {
            return new RequestNotificationResponse(notificationService.approveByApprovalRequestId(notificationAnswerRequest, approver.getId()));
        }
        else if(notificationAnswerRequest.getApproved() == -1)
        {
            return new RequestNotificationResponse(notificationService.disapproveByApprovalRequestId(notificationAnswerRequest, approver.getId()));
        }
        return new RequestNotificationResponse(notificationService.removeRequestNotification(requestNotification));
    }

    /* TODO: delete this sometime */
    @Autowired
    private RequestNotificationRepository requestNotificationRepository;
    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;

    @RequestMapping(value = "/add/{userid}/{userskillid}/{levelid}", method = RequestMethod.POST )
    public @ResponseBody
    String addRequest(@PathVariable Long userid, @PathVariable Long userskillid, @PathVariable Integer levelid)
    {
        RequestNotification requestNotification = new RequestNotification();
        List<RequestNotification> list = new LinkedList<>();
        list.add(requestNotification);

        requestNotification.setReceiver(userService.getUserById(userid));
        requestNotificationRepository.save(requestNotification);


        ApprovalRequest approvalRequest= new ApprovalRequest(list);
        approvalRequest.setUserSkillLevel(userSkillRepository.findOne(userskillid).getUserSkillLevels().get(levelid));
        approvalRequestRepository.save(approvalRequest);

        requestNotification.setApprovalRequest(approvalRequest);
        requestNotificationRepository.save(requestNotification);
        return "success";
    }
}


