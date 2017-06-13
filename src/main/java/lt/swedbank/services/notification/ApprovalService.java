package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSkillLevelService userSkillLevelService;

    public ApprovalRequest approve(Long approvalRequestId, Long approverId) {
        ApprovalRequest request = approvalRequestRepository.findOne(approvalRequestId);
        request.approve();
        request.addApprover(userService.getUserById(approverId));
        if(request.getApproves() >= 5)
        {
            deleteNotifications(request);
            userSkillLevelService.levelUp(approvalRequestRepository.findOne(approvalRequestId).getUserSkillLevel());
        }
        approvalRequestRepository.save(request);
        return request;
    }



    private void deleteNotifications(ApprovalRequest request) {
        notificationService.deleteNotifications(request);
    }

    public ApprovalRequest disapprove(Long approvalRequestId, Long approverId) {

        ApprovalRequest request = approvalRequestRepository.findOne(approvalRequestId);
        request.disapprove();
        request.setDisapprover(userService.getUserById(approverId));
        approvalRequestRepository.save(request);
        return request;
    }

}
