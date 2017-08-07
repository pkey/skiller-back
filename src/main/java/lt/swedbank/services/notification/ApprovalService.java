package lt.swedbank.services.notification;


import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.notification.ApproverNotFoundException;
import lt.swedbank.exceptions.notification.DisapproverNotFoundException;
import lt.swedbank.exceptions.user.UserNotFoundException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ApprovalService {

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;
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

    @Value("${notification.approves_needed}")
    public  Integer APPROVES_NEEDED;
    @Value("${notification.disapproves_needed}")
    public  Integer DISAPPROVES_NEEDED;
    @Value("${notification.min_amount_of_users_required}")
    public  Integer MIN_AMOUNT_OF_NOTIFIED_USERS_REQUIRED;

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


        if (usersToBeNotified.size() < MIN_AMOUNT_OF_NOTIFIED_USERS_REQUIRED) {
            usersToBeNotified = (List<User>) userService.getAllUsers();
        }

        List<RequestNotification> notifications = new ArrayList<>();
        usersToBeNotified.stream()
                .filter(user -> !user.getId().equals(userId))
                .forEach(user -> notifications.add(new RequestNotification(user, approvalRequest)));
        return notifications;
    }

    public boolean isUserAlreadyApprovedReqest(User user, ApprovalRequest approvalRequest) {
        for (Approver approver : approvalRequest.getApprovers()) {
            if (approver.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserAlreadyDissapprovedRequest(User user, ApprovalRequest approvalRequest) {
        for (Disapprover disapprover : approvalRequest.getDisapprovers()) {
            if (disapprover.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public void removeUserFromApproversAndDisapproversIfExists(ApprovalRequest approvalRequest, User user) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (isUserAlreadyDissapprovedRequest(user, approvalRequest)) {
            removeDissapproverFromApprovalRequest(user, approvalRequest);
        } else if (isUserAlreadyApprovedReqest(user, approvalRequest)) {
            removeApproverFromApprovalRequest(user, approvalRequest);
        }
    }


    public ApprovalRequest approve(String message, ApprovalRequest approvalRequest, User user) {

        removeUserFromApproversAndDisapproversIfExists(approvalRequest, user);
        Approver approver = new Approver(user, message);
        approversRepository.save(approver);
        approvalRequest.addApprover(approver);

        if (approvalRequest.getApprovers().size() >= APPROVES_NEEDED) {
            approvalRequest.setApproved();
        }
        return approvalRequestRepository.save(approvalRequest);
    }


    public ApprovalRequest disapprove(String message, ApprovalRequest approvalRequest, User user) {

        removeUserFromApproversAndDisapproversIfExists(approvalRequest, user);
        Disapprover disapprover = new Disapprover(user, message);
        disaproversRepository.save(disapprover);
        approvalRequest.addDisapprover(disapprover);

        if (approvalRequest.getDisapprovers().size() >= DISAPPROVES_NEEDED) {
            approvalRequest.setDisapproved();
        }
        return approvalRequestRepository.save(approvalRequest);
    }

    public ApprovalRequest getApprovalRequestByRequestNotification(RequestNotification notification) {
        return approvalRequestRepository.findOne(notification.getApprovalRequest().getId());
    }

    public ApprovalRequest update(ApprovalRequest approvalRequest) {
        return approvalRequestRepository.save(approvalRequest);
    }

    private void removeApproverFromApprovalRequest(User user, ApprovalRequest approvalRequest) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        Approver currentApprover = getApproverById(approvalRequest.getApprovers().stream()
                .filter(approver -> approver.getUser().equals(user))
                .findFirst()
                .get().getId());

        approvalRequest.getApprovers().remove(currentApprover);
        approversRepository.delete(currentApprover.getId());
    }


    public void removeDissapproverFromApprovalRequest(User user, ApprovalRequest approvalRequest) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        Disapprover currentDisapprover = getDisapproverById(approvalRequest.getDisapprovers().stream()
                .filter(disapprover -> disapprover.getUser().equals(user))
                .findFirst()
                .get().getId());

        approvalRequest.getDisapprovers().remove(currentDisapprover);
        disaproversRepository.delete(currentDisapprover.getId());
    }

    public Approver saveApprover(Approver approver) {
        return approversRepository.save(approver);
    }

    public Disapprover saveDisapprover(Disapprover disapprover) {
        return disaproversRepository.save(disapprover);
    }

    public Disapprover getDisapproverById(Long id) {
        Disapprover disapprover = disaproversRepository.findOne(id);
        if (disapprover == null) {
            throw new DisapproverNotFoundException();
        }
        return disapprover;
    }

    public Approver getApproverById(Long id) {
        Approver approver = approversRepository.findOne(id);
        if (approver == null) {
            throw new ApproverNotFoundException();
        }
        return approver;
    }
}
