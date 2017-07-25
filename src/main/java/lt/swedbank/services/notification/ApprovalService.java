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
            if (!user.getId().equals(userId)) {
                notifications.add(new RequestNotification(user, approvalRequest));
            }
        }

        return notifications;
    }

    public Approver saveApprover(Approver approver) {
        return approversRepository.save(approver);
    }


    public boolean isUserAlreadyApprovedReqest(User user, ApprovalRequest approvalRequest)
    {
        for (Approver approver: approvalRequest.getApprovers()) {
            if(approver.getUser() == user) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserAlreadyDissapprovedRequest(User user, ApprovalRequest approvalRequest)
    {
        for (Disapprover disapprover: approvalRequest.getDisapprovers()) {
            if(disapprover.getUser() == user) {
                return true;
            }
        }
        return false;
    }

    public void removeDissapproverFromApprovalRequest(User user, ApprovalRequest approvalRequest)
    {
        for (Disapprover disapprover: approvalRequest.getDisapprovers()
             ) {
            if(disapprover.getUser() == user)
            {
                disaproversRepository.delete(disapprover.getId());
            }
        }
    }

    public void removeApproverFromApprovalRequest(User user, ApprovalRequest approvalRequest)
    {
        for (Approver approver: approvalRequest.getApprovers()) {
            if(approver.getUser() == user)
            {
                disaproversRepository.delete(approver.getId());
            }
        }
    }


    public ApprovalRequest approve(String message, ApprovalRequest approvalRequest, User user) {

        if(isUserAlreadyDissapprovedRequest(user, approvalRequest)) {
            removeDissapproverFromApprovalRequest(user, approvalRequest);
        }

        if (approvalRequest.isApproved() == 0 && !isUserAlreadyApprovedReqest(user, approvalRequest)) {
            Approver approver = new Approver(user, message);
            saveApprover(approver);
            approvalRequest.addApprover(approver);
        }

        if (approvalRequest.getApproves() >= 5) {
            approvalRequest.setIsApproved(1);
            notificationService.setNotificationsAsExpired(approvalRequest.getRequestNotifications());
        }
        return approvalRequestRepository.save(approvalRequest);
    }

    public Disapprover saveDisapprover(Disapprover disapprover) {
        return disaproversRepository.save(disapprover);
    }

    public ApprovalRequest disapprove(String message, ApprovalRequest approvalRequest, User user) {

        if (approvalRequest.isApproved() == 0) {

            Disapprover disapprover = new Disapprover(user, message);
            saveDisapprover(disapprover);
            approvalRequest.addDisapprover(disapprover);
            approvalRequest.setIsApproved(-1);
            notificationService.setNotificationsAsExpired(approvalRequest.getRequestNotifications());
        }
        return approvalRequestRepository.save(approvalRequest);
    }

    public ApprovalRequest getApprovalRequestByRequestNotification(RequestNotification notification) {
        return approvalRequestRepository.findOne(notification.getApprovalRequest().getId());
    }

    public ApprovalRequest update(ApprovalRequest approvalRequest) {
        return approvalRequestRepository.save(approvalRequest);
    }
}
