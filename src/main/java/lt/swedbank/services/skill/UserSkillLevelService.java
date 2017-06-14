package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.userSkillLevel.UserSkillLevelNotFoundException;
import lt.swedbank.repositories.UserSkillLevelRepository;
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

    public UserSkillLevel getCurrentUserSkillLevelByUserIdAndSkillId(Long userId, Long skillId) throws UserSkillLevelNotFoundException {
        UserSkill userSkill = userSkillService.getUserSkillByUserIdAndSkillId(userId, skillId);
        UserSkillLevel userSkillLevel = userSkillLevelRepository.findTopByUserSkillOrderByValidFromDesc(userSkill);

        if (userSkillLevel == null) {
            throw new UserSkillLevelNotFoundException();
        }

        return userSkillLevel;
    }

    public UserSkillLevel addDefaultUserSkillLevel(UserSkill userSkill) {
        UserSkillLevel userSkillLevel = new UserSkillLevel(userSkill, skillLevelService.getDefault());
        return userSkillLevelRepository.save(userSkillLevel);
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

    public Iterable<UserSkillLevel> getAllBySkillLevel(SkillLevel skillLevel) {
        return userSkillLevelRepository.findAllBySkillLevel(skillLevel);
    }

    public Set<UserSkillLevel> getAllUserSkillLevelsSetBySkillLevels(Iterable<SkillLevel> skillLevels) {
        Set<UserSkillLevel> userSkillLevels = new HashSet<>();
        for (SkillLevel skillLevel : skillLevels) {
            Iterable<UserSkillLevel> oneLevelSkillLevels = getAllBySkillLevel(skillLevel);

            oneLevelSkillLevels.forEach(userSkillLevels::add);
        }

        return userSkillLevels;
    }
}


