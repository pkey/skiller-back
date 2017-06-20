package lt.swedbank.services.notification;


import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.exceptions.userSkillLevel.RequestAlreadySubmittedException;
import lt.swedbank.exceptions.userSkillLevel.TooHighSkillLevelRequestException;
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

    public ApprovalRequest addSkillLevelApprovalRequestWithNotifications(Long userId, AssignSkillLevelRequest assignSkillLevelRequest) throws RequestAlreadySubmittedException, TooHighSkillLevelRequestException {

        if (userSkillLevelService.isLatestUserSkillLevelPending(userId, assignSkillLevelRequest.getSkillId())) {
            throw new RequestAlreadySubmittedException();
        }
        if(assignSkillLevelRequest.getLevelId() -
                userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(userId, assignSkillLevelRequest.getSkillId())
                .getSkillLevel().getLevel() > 1) {
            throw new TooHighSkillLevelRequestException();
        }

        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, assignSkillLevelRequest.getSkillId());
        UserSkillLevel newUserSkillLevel = userSkillLevelService.addUserSkillLevel(userSkill, assignSkillLevelRequest);

        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setUserSkillLevel(newUserSkillLevel);
        approvalRequest.setMotivation(assignSkillLevelRequest.getMotivation());

        approvalRequest.setRequestNotifications(createNotifications(userId, assignSkillLevelRequest, approvalRequest));
        approvalRequestRepository.save(approvalRequest);
        return approvalRequest;
    }

    private List<RequestNotification> createNotifications(Long userId, AssignSkillLevelRequest assignSkillLevelRequest, ApprovalRequest approvalRequest) {

        Iterable<SkillLevel> skillLevels = skillLevelService.getAllByLevelGreaterThanOrEqual(assignSkillLevelRequest.getLevelId());
        Iterable<UserSkillLevel> userSkillLevels = userSkillLevelService.getAllApprovedUserSkillLevelsBySkillLevels(skillLevels);

        List<User> usersToBeNotified = new ArrayList<>();
        Long skillIdFromRequest = approvalRequest.getUserSkillLevel().getUserSkill().getSkill().getId();
        for (UserSkillLevel u : userSkillLevels) {
            Long skillId = u.getUserSkill().getSkill().getId();
            User user = u.getUserSkill().getUser();
            if (skillId.equals(skillIdFromRequest) && !user.getId().equals(userId)) {
                usersToBeNotified.add(user);
            }
        }

        List<RequestNotification> notifications = new ArrayList<>();
        for (User user : usersToBeNotified) {
            notifications.add(new RequestNotification(user, approvalRequest));
        }

        return notifications;
    }

    public ApprovalRequest approve(NotificationAnswerRequest notificationAnswerRequest, ApprovalRequest request, Long approverId) {

        if(request.isApproved() == 0) {
            request.addApprover(new Approver(userService.getUserById(approverId), notificationAnswerRequest.getMessage()));
            RequestNotification notification = notificationService.getNotificationById(notificationAnswerRequest.getNotificationId());
            notificationService.removeRequestNotification(notification);
            request.removeNotification(notification);
        }

        if(request.getApproves() >= 5) {
            request.setIsApproved(1);
            request.setRequestNotifications(null);
        }
        return approvalRequestRepository.save(request);
    }


    public ApprovalRequest disapprove(String message, RequestNotification requestNotificationFromApprovalRequest, Long disapproverId) {

        ApprovalRequest request = getApprovalRequestByRequestNotification(requestNotificationFromApprovalRequest);
        if(request.isApproved() == 0) {

            request.setDisapprover(new Disapprover(userService.getUserById(disapproverId), message));
            request.setIsApproved(-1);
            request.setRequestNotifications(null);
        }
        return approvalRequestRepository.save(request);
    }

    public ApprovalRequest getApprovalRequestByRequestNotification(RequestNotification notification) {
        return approvalRequestRepository.findOne(notification.getApprovalRequest().getId());
    }

    public ApprovalRequest update(ApprovalRequest approvalRequest) {
        return approvalRequestRepository.save(approvalRequest);
    }
}
