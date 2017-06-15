package lt.swedbank.services.notification;


import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.request.RequestAlreadySubmittedException;
import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.services.skill.SkillLevelService;
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
    private SkillLevelService skillLevelService;
    @Autowired
    private UserSkillLevelService userSkillLevelService;

    public ApprovalRequest createSkillLevelApprovalRequest(Long userId, AssignSkillLevelRequest assignSkillLevelRequest) throws RequestAlreadySubmittedException {

        UserSkillLevel userSkillLevel = userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(userId, assignSkillLevelRequest.getSkillId());

        if (approvalRequestRepository.findByUserSkillLevel(userSkillLevel) != null) {
            throw new RequestAlreadySubmittedException();
        }

        ApprovalRequest approvalRequest = new ApprovalRequest();

        approvalRequest.setUserSkillLevel(userSkillLevel);
        approvalRequest.setMotivation(assignSkillLevelRequest.getMotivation());

        Iterable<SkillLevel> skillLevels = skillLevelService.getAllByLevelGreaterThan(assignSkillLevelRequest.getLevelId());
        Iterable<UserSkillLevel> userSkillLevels = userSkillLevelService.getAllUserSkillLevelsSetBySkillLevels(skillLevels);

        List<User> usersToBeNotified = new ArrayList<>();
        Long skillIdFromRequest = approvalRequest.getUserSkillLevel().getUserSkill().getSkill().getId();
        for (UserSkillLevel u : userSkillLevels) {

            Long skillId = u.getUserSkill().getSkill().getId();
            if (skillId.equals(skillIdFromRequest)) {
                usersToBeNotified.add(u.getUserSkill().getUser());
            }
        }

        List<RequestNotification> notifications = new ArrayList<>();
        for (User user : usersToBeNotified) {
            notifications.add(new RequestNotification(user, approvalRequest));
        }

        approvalRequest.setRequestNotifications(notifications);
        approvalRequestRepository.save(approvalRequest);
        /* TODO ask about a way how to persist notifications since the approvalRequest has "cascade type all" strategy for notifications that this request stores
            notificationService.addNotifications(approvalRequest);*/
        return approvalRequest;
    }

    public ApprovalRequest approve(NotificationAnswerRequest notificationAnswerRequest, Long approvalRequestId, Long approverId) {
        ApprovalRequest request = approvalRequestRepository.findOne(approvalRequestId);
        request.approve();
        request.addApprover(userService.getUserById(approverId));

        RequestNotification notification = notificationService.getNotificationById(notificationAnswerRequest.getNotificationId());
        request.getRequestNotifications().remove(notification);

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
        request.setRequestNotifications(null);
        request.setDisapprover(userService.getUserById(approverId));
        approvalRequestRepository.save(request);
        return request;
    }

}
