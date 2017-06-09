package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.userSkillLevel.UserSkillLevelNotFoundException;
import lt.swedbank.repositories.UserSkillLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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

    public UserSkillLevel getCurrentUserSkillLevelFromList(List<UserSkillLevel> userSkillLevelList){
        UserSkillLevel currentUserSkillLevel;

        if(userSkillLevelList == null){
            return null;
        }
        userSkillLevelList.sort(new Comparator<UserSkillLevel>() {
            @Override
            public int compare(UserSkillLevel o1, UserSkillLevel o2) {
                return o2.getValidFrom().compareTo(o1.getValidFrom());
            }
        });

        currentUserSkillLevel = userSkillLevelList.get(0);

        return currentUserSkillLevel;
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


}


