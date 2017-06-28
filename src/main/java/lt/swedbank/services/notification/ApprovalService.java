package lt.swedbank.services.notification;


import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.exceptions.userSkillLevel.RequestAlreadySubmittedException;
import lt.swedbank.exceptions.userSkillLevel.TooHighSkillLevelRequestException;
import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.repositories.ApproversRepository;
import lt.swedbank.repositories.DisaproversRepository;
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
    private ApproversRepository approversRepository;
    @Autowired
    private DisaproversRepository disaproversRepository;
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
        if (assignSkillLevelRequest.getLevelId() - userSkillLevelService
                .getCurrentUserSkillLevelByUserIdAndSkillId(userId, assignSkillLevelRequest.getSkillId())
                .getSkillLevel().getLevel() > 1) {
            throw new TooHighSkillLevelRequestException();
        }

        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, assignSkillLevelRequest.getSkillId());
        UserSkillLevel newUserSkillLevel = userSkillLevelService.addUserSkillLevel(userSkill, assignSkillLevelRequest);

        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setUserSkillLevel(newUserSkillLevel);
        approvalRequest.setMotivation(assignSkillLevelRequest.getMotivation());

        approvalRequest.setRequestNotifications(createNotificationsForUsersWithTheSameSkill(userId, assignSkillLevelRequest, approvalRequest));
        approvalRequestRepository.save(approvalRequest);
        return approvalRequest;
    }

    private List<RequestNotification> createNotificationsForUsersWithTheSameSkill(Long userId, AssignSkillLevelRequest assignSkillLevelRequest, ApprovalRequest approvalRequest) {

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

        if (usersToBeNotified.size() < 5) {
            usersToBeNotified = userService.getAllUsers();
        }

        List<RequestNotification> notifications = new ArrayList<>();
        for (User user : usersToBeNotified) {
            notifications.add(new RequestNotification(user, approvalRequest));
        }


        return notifications;
    }

    public Approver addApprover(Approver approver) {
        return approversRepository.save(approver);
    }


    public ApprovalRequest approve(NotificationAnswerRequest notificationAnswerRequest, ApprovalRequest request, Long approverId) {

        if (request.isApproved() == 0) {
            Approver approver = new Approver(userService.getUserById(approverId), notificationAnswerRequest.getMessage());
            addApprover(approver);
            request.addApprover(approver);
            RequestNotification notification = notificationService.getNotificationById(notificationAnswerRequest.getNotificationId());
            notificationService.removeRequestNotification(notification);
            request.removeNotification(notification);
        }

        if (request.getApproves() >= 5) {
            request.setIsApproved(1);
            request.setRequestNotifications(null);
        }
        return approvalRequestRepository.save(request);
    }

    public Disapprover addDisapprover(Disapprover disapprover) {
        return disaproversRepository.save(disapprover);
    }

    public ApprovalRequest disapprove(String message, RequestNotification requestNotificationFromApprovalRequest, Long disapproverId) {

        ApprovalRequest request = getApprovalRequestByRequestNotification(requestNotificationFromApprovalRequest);
        if (request.isApproved() == 0) {

            Disapprover disapprover = new Disapprover(userService.getUserById(disapproverId), message);
            addDisapprover(disapprover);
            request.setDisapprover(disapprover);
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
