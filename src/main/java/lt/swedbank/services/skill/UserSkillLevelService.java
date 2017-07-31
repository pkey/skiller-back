package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.enums.Status;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.userSkillLevel.UserSkillLevelNotFoundException;
import lt.swedbank.repositories.UserSkillLevelRepository;
import lt.swedbank.services.notification.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserSkillLevelService {

    @Autowired
    private UserSkillLevelRepository userSkillLevelRepository;
    @Autowired
    private SkillLevelService skillLevelService;
    @Autowired
    private UserSkillService userSkillService;
    @Autowired
    private ApprovalService approvalService;

    public UserSkillLevel getCurrentUserSkillLevelByUserIdAndSkillId(Long userId, Long userSkillId) throws UserSkillLevelNotFoundException {
        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, userSkillId);

        UserSkillLevel userSkillLevel =
                userSkillLevelRepository.findTopByUserSkillAndStatusOrderByValidFromDesc(userSkill, Status.APPROVED);

        if (userSkillLevel == null) {
            throw new UserSkillLevelNotFoundException();
        }

        return userSkillLevel;
    }

    public boolean isLatestUserSkillLevelPending(Long userId, Long skillId) throws UserSkillLevelNotFoundException {
        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, skillId);

        UserSkillLevel lastPendingUserSkillLevel =
                userSkillLevelRepository.findTopByUserSkillAndStatusOrderByValidFromDesc(userSkill, Status.PENDING);

        if (lastPendingUserSkillLevel == null) {
            return false;
        }

        UserSkillLevel currentUserSkillLevel = getCurrentUserSkillLevelByUserIdAndSkillId(userId, skillId);
        return !currentUserSkillLevel.getValidFrom().after(lastPendingUserSkillLevel.getValidFrom());
    }

    public UserSkillLevel addDefaultUserSkillLevel(UserSkill userSkill) {
        UserSkillLevel userSkillLevel = new UserSkillLevel(userSkill, skillLevelService.getDefault());
        userSkillLevelRepository.save(userSkillLevel);

        return userSkillLevel;
    }

    public UserSkillLevel addUserSkillLevel(UserSkill userSkill, AssignSkillLevelRequest assignSkillLevelRequest) {
        UserSkillLevel userSkillLevel = new UserSkillLevel(userSkill,
                skillLevelService.getByLevel(assignSkillLevelRequest.getLevelId()));

        userSkillLevel.setMotivation(assignSkillLevelRequest.getMotivation());
        userSkillLevel.setPending();

        return userSkillLevelRepository.save(userSkillLevel);
    }

    public UserSkillLevel levelUp(UserSkillLevel userSkillLevel) {
        Long oldLevel = userSkillLevel.getSkillLevel().getLevel().longValue();
        userSkillLevel.setSkillLevel(skillLevelService.getByLevel(oldLevel + 1));
        return userSkillLevelRepository.save(userSkillLevel);
    }

    public Iterable<UserSkillLevel> getAllByOneSkillLevelAndStatus(SkillLevel skillLevel, Status status) {
        return userSkillLevelRepository.findAllBySkillLevelAndStatus(skillLevel, status);
    }

    public Set<UserSkillLevel> getAllApprovedUserSkillLevelsBySkillLevels(Iterable<SkillLevel> skillLevels) {
        Set<UserSkillLevel> userSkillLevels = new HashSet<>();
        for (SkillLevel skillLevel : skillLevels) {
            Iterable<UserSkillLevel> oneLevelSkillLevels = getAllByOneSkillLevelAndStatus(skillLevel, Status.APPROVED);

            oneLevelSkillLevels.forEach(userSkillLevels::add);
        }

        return userSkillLevels;
    }
}


