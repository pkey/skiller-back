package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
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

    public UserSkillLevel getCurrentUserSkillLevelByUserIdAndSkillId(Long userId, Long skillId) throws UserSkillLevelNotFoundException {
        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, skillId);

        UserSkillLevel userSkillLevel =
                userSkillLevelRepository.findTopByUserSkillAndIsApprovedOrderByValidFromDesc(userSkill, 1);

        if (userSkillLevel == null) {
            throw new UserSkillLevelNotFoundException();
        }

        return userSkillLevel;
    }

    public boolean isLatestUserSkillLevelPending(Long userId, Long skillId) throws UserSkillLevelNotFoundException {
        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, skillId);

        UserSkillLevel lastPendingUserSkillLevel =
                userSkillLevelRepository.findTopByUserSkillAndIsApprovedOrderByValidFromDesc(userSkill, 0);

        if (lastPendingUserSkillLevel == null) {
            return false;
        }

        UserSkillLevel currentUserSkillLevel = getCurrentUserSkillLevelByUserIdAndSkillId(userId, skillId);
        return !currentUserSkillLevel.getValidFrom().after(lastPendingUserSkillLevel.getValidFrom());
    }

    public UserSkillLevel addDefaultUserSkillLevel(UserSkill userSkill) {
        UserSkillLevel userSkillLevel = new UserSkillLevel(userSkill, skillLevelService.getDefault());

        approvalService.addDefaultApprovalRequest(userSkillLevel);
        userSkillLevelRepository.save(userSkillLevel);

        return userSkillLevel;
    }

    public UserSkillLevel addUserSkillLevel(UserSkill userSkill, AssignSkillLevelRequest assignSkillLevelRequest) {
        UserSkillLevel userSkillLevel = new UserSkillLevel(userSkill,
                skillLevelService.getByLevel(assignSkillLevelRequest.getLevelId()));

        userSkillLevel.setMotivation(assignSkillLevelRequest.getMotivation());

        return userSkillLevelRepository.save(userSkillLevel);
    }

    public UserSkillLevel levelUp(UserSkillLevel userSkillLevel) {
        Long oldLevel = userSkillLevel.getSkillLevel().getLevel().longValue();
        userSkillLevel.setSkillLevel(skillLevelService.getByLevel(oldLevel + 1));
        return userSkillLevelRepository.save(userSkillLevel);
    }

    public Iterable<UserSkillLevel> getAllByOneSkillLevelAndIsApproved(SkillLevel skillLevel, Integer isApproved) {
        return userSkillLevelRepository.findAllBySkillLevelAndIsApproved(skillLevel, isApproved);
    }

    public Set<UserSkillLevel> getAllApprovedUserSkillLevelsBySkillLevels(Iterable<SkillLevel> skillLevels) {
        Set<UserSkillLevel> userSkillLevels = new HashSet<>();
        for (SkillLevel skillLevel : skillLevels) {
            Iterable<UserSkillLevel> oneLevelSkillLevels = getAllByOneSkillLevelAndIsApproved(skillLevel, 1);

            oneLevelSkillLevels.forEach(userSkillLevels::add);
        }

        return userSkillLevels;
    }
}


