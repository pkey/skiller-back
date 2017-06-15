package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.services.skill.SkillLevelService;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillLevelService skillLevelService;
    @Autowired
    private UserSkillLevelService userSkillLevelService;

    public ApprovalRequest createSkillLevelApprovalRequest(Long userId, AssignSkillLevelRequest assignSkillLevelRequest) {

        ApprovalRequest approvalRequest = new ApprovalRequest();
        UserSkillLevel userSkillLevel = userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(userId, assignSkillLevelRequest.getSkillId());

        approvalRequest.setUserSkillLevel(userSkillLevel);
        approvalRequest.setMotivation(assignSkillLevelRequest.getMotivation());

        Iterable<SkillLevel> skillLevels = skillLevelService.getAllByLevelGreaterThan(assignSkillLevelRequest.getLevelId());
        Iterable<UserSkillLevel> userSkillLevels = userSkillLevelService.getAllUserSkillLevelsSetBySkillLevels(skillLevels);

        List<User> usersToBeNotified = new ArrayList<>();
        Long skillFromRequestId = approvalRequest.getUserSkillLevel().getUserSkill().getSkill().getId();
        for (UserSkillLevel u : userSkillLevels) {

            Long skillId = u.getUserSkill().getSkill().getId();
            if(skillId.equals(skillFromRequestId)) {
                usersToBeNotified.add(u.getUserSkill().getUser());
            }
        }

        List<RequestNotification> notifications = new ArrayList<>();
        for (User user : usersToBeNotified) {
            notifications.add(new RequestNotification(user, approvalRequest));
        }

        approvalRequest.setRequestNotification(notifications);
        approvalRequestRepository.save(approvalRequest);
        //TODO ask about a way how to persist notifications since the approvalRequest has "cascade type all" strategy for notifications that this request stores
        //notificationService.addNotifications(approvalRequest);
        return approvalRequest;
    }

    public ApprovalRequest approve(Long approvalRequestId, Long approverId) {
        ApprovalRequest request = approvalRequestRepository.findOne(approvalRequestId);
        request.approve();
        request.addApprover(userService.getUserById(approverId));
        if (request.getApproves() >= 5) {
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
