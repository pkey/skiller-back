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

    public ApprovalRequest createAndSaveSkillLevelApprovalRequest(Long userId, AssignSkillLevelRequest request) {

        ApprovalRequest approvalRequest = new ApprovalRequest();
        //Gaunu userSkillLevel.user pagal skill id
        //pagal level id gausiu skill level
        UserSkillLevel userSkillLevel = userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(userId, request.getSkillId());

        approvalRequest.setUserSkillLevel(userSkillLevel);
        approvalRequest.setMotivation(request.getMotivation());

        Iterable<SkillLevel> skillLevels = skillLevelService.getAllByLevelGreaterThan(request.getLevelId());
        Iterable<UserSkillLevel> userSkillLevels = userSkillLevelService.getAllUserSkillLevelsSetBySkillLevels(skillLevels);

        List<User> usersToBeNotified = new ArrayList<>();
        for (UserSkillLevel u : userSkillLevels) {
            usersToBeNotified.add(u.getUserSkill().getUser());
        }

        List<RequestNotification> notifications = new ArrayList<>();
        for (User user : usersToBeNotified) {
            notifications.add(new RequestNotification(user, approvalRequest));
        }
        notificationService.addNotifications(notifications);
        approvalRequest.setRequestNotification(notifications);

        approvalRequestRepository.save(approvalRequest);
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
