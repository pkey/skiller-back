package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.repositories.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSkillService {

    @Autowired
    private UserSkillRepository userSkillRepository;
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillLevelService skillLevelService;


    public UserSkill addUserSkill(User user, AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        Skill skill = skillService.findByTitle(addSkillRequest.getTitle());

        if(userSkillAlreadyExists(user.getId(), skill)) {
            throw new SkillAlreadyExistsException();
        }

        UserSkill userSkill = new UserSkill(user, skill);
        userSkill.setSkillLevel(skillLevelService.getDefault());
        userSkillRepository.save(userSkill);

        return userSkill;
    }

    public UserSkill removeUserSkill(Long userId, RemoveSkillRequest removeSkillRequest) throws SkillNotFoundException {

        Skill skill = skillService.findByTitle(removeSkillRequest.getTitle());

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skill.getId());

        if(userSkill == null){
            throw new SkillNotFoundException();
        }

        userSkillRepository.delete(userSkill);

        return userSkill;
    }

    public UserSkill assignSkillLevel(Long userID, AssignSkillLevelRequest request){

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userID, request.getSkillId());
        userSkill.setMotivation(request.getMotivation());

        userSkill.setSkillLevel(skillLevelService.getById(request.getLevelId()));
        userSkillRepository.save(userSkill);

        return userSkill;
    }

    private boolean userSkillAlreadyExists(Long userID, Skill skill) {
        return Optional.ofNullable(userSkillRepository.findByUserIdAndSkillId(userID, skill.getId())).isPresent();
    }
}
