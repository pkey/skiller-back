package lt.swedbank.services.notification;


import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.exceptions.request.RequestAlreadySubmittedException;
import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.services.skill.SkillLevelService;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.skill.UserSkillService;
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
    private UserSkillService userSkillService;
    @Autowired
    private SkillLevelService skillLevelService;
    @Autowired
    private UserSkillLevelService userSkillLevelService;

    public ApprovalRequest addDefaultApprovalRequest(UserSkillLevel userSkillLevel) {
        ApprovalRequest defaultApprovalRequest = new ApprovalRequest();
        defaultApprovalRequest.setUserSkillLevel(userSkillLevel);
        defaultApprovalRequest.setIsApproved(1);
        return approvalRequestRepository.save(defaultApprovalRequest);
    }

    public ApprovalRequest createSkillLevelApprovalRequest(Long userId, AssignSkillLevelRequest assignSkillLevelRequest) throws RequestAlreadySubmittedException {

        if (userSkillLevelService.isLatestUserSkillLevelPending(userId, assignSkillLevelRequest.getSkillId())) {
            throw new RequestAlreadySubmittedException();
        }

        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, assignSkillLevelRequest.getSkillId());
        UserSkillLevel newUserSkillLevel = userSkillLevelService.addUserSkillLevel(userSkill, assignSkillLevelRequest);

        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setUserSkillLevel(newUserSkillLevel);
        approvalRequest.setMotivation(assignSkillLevelRequest.getMotivation());

        Iterable<SkillLevel> skillLevels = skillLevelService.getAllByLevelGreaterThanOrEqual(assignSkillLevelRequest.getLevelId());
        Iterable<UserSkillLevel> userSkillLevels = userSkillLevelService.getAllUserSkillLevelsBySkillLevels(skillLevels);

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
        return approvalRequest;
    }

    public ApprovalRequest approve(NotificationAnswerRequest notificationAnswerRequest, ApprovalRequest request, Long approverId) {

        if(request.isApproved() == 0) {
            request.addApprover(userService.getUserById(approverId));
            RequestNotification notification = notificationService.getNotificationById(notificationAnswerRequest.getNotificationId());
            notificationService.removeRequestNotification(notification);
            request.removeNotification(notification);
        }

        if(request.getApproves() >= 5)
        {
            request.setIsApproved(1);
            deleteNotifications(request);
            userSkillLevelService.levelUp(approvalRequestRepository.findOne(request.getId()).getUserSkillLevel());
        }
        approvalRequestRepository.save(request);
        return request;
    }

    private void deleteNotifications(ApprovalRequest request) {
        notificationService.deleteNotifications(request);
    }

    public ApprovalRequest getApprovalRequestByRequestNotification(RequestNotification notification)
    {
        return approvalRequestRepository.findOne(notification.getApprovalRequest().getId());
    }

    public ApprovalRequest disapprove(ApprovalRequest request, Long approverId) {

        if(request.isApproved() == 0)
        {
            request.setDisapprover(userService.getUserById(approverId));
            request.setIsApproved(-1);
            deleteNotifications(request);
            request.setRequestNotifications(null);
            approvalRequestRepository.save(request);
        }

        return request;
    }

    public ApprovalRequest update(ApprovalRequest approvalRequest) {
        return approvalRequestRepository.save(approvalRequest);
    }
}
