package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.skill.SkillLevelDoesNotExist;
import lt.swedbank.repositories.UserSkillLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSkillLevelService {

    @Autowired
    private UserSkillLevelRepository userSkillLevelRepository;
    @Autowired
    private SkillLevelService skillLevelService;


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

}
