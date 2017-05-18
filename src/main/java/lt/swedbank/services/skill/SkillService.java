package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService implements ISkillService {

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;

    public SkillService(SkillRepository skillRepository, UserSkillRepository userSkillRepository) {
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }

    @Override
    public UserSkill addSkill(Long userID, AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        Skill skill = skillRepository.findByTitle(addSkillRequest.getTitle());

        if(skill == null) {
            skill = new Skill(addSkillRequest.getTitle());
            skillRepository.save(skill);
        } else {
            skill = skillRepository.findByTitle(addSkillRequest.getTitle());
        }

        if(isUserSkillAlreadyExists(userID, skill)) {
            throw new SkillAlreadyExistsException();
        }

        UserSkill userSkill = new UserSkill(userID, skill);
        userSkillRepository.save(userSkill);

        return userSkill;
    }

    @Override
    public UserSkill removeSkill(Long userID, Skill skill) throws SkillNotFoundException {

        UserSkill userSkill = userSkillRepository.findByUserIDAndSkill(userID, skill);

        if(userSkill == null){
            throw new SkillNotFoundException();
        }

        userSkillRepository.delete(userSkill);

        return userSkill;
    }

    private boolean isUserSkillAlreadyExists(Long userID, Skill skill) {
        return Optional.ofNullable(userSkillRepository.findByUserIDAndSkill(userID, skill)).isPresent();
    }

}
