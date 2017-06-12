package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.response.RequestNotificationResponse;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NotificationService {

    @Autowired
    private RequestNotificationRepository requestNotificationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSkillService userSkillService;
    @Autowired
    private ApprovalService approvalService;

    public Iterable<RequestNotification> getNotificationsByUserId(Long id)
    {
        return requestNotificationRepository.findByReceiverAndAnsweredFalse(userService.getUserById(id));
    }

    public ArrayList<RequestNotificationResponse> getRequestNotificationResponse(Iterable<RequestNotification> requestNotifications)
    {
        ArrayList<RequestNotificationResponse> requestNotificationResponses = new ArrayList<RequestNotificationResponse>();
        for (RequestNotification requestNotification : requestNotifications ) {
            requestNotificationResponses.add(new RequestNotificationResponse(requestNotification));
        }
        return requestNotificationResponses;
    }


    public RequestNotification  approveByApprovalRequestId(Long id) {
        RequestNotification request = requestNotificationRepository.findOne(id);
        requestNotificationRepository.delete(request);
        approvalService.approve(request.getApprovalRequest().getId());
        return request;
    }

    public RequestNotification disapproveByApprovalRequestId(Long id) {
        RequestNotification request = requestNotificationRepository.findOne(id);
        request.setAnswered(true);
        requestNotificationRepository.save(request);
        approvalService.disapprove(request.getApprovalRequest().getId());
        return request;
    }

//    public void assignNotifications(ApprovalRequest approvalRequest)
//    {
//        for (UserSkill userSkill: userSkillService.getAllUserSkillsBySkill(approvalRequest.getUserSkill().getSkill())
//             ) {
//
//        }
//    }

}
