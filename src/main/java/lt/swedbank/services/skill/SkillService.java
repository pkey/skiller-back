package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.skill.*;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {


    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;


    public SkillService(SkillRepository skillRepository, UserSkillRepository userSkillRepository) {
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }


    public UserSkill addSkill(Long userID, AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        UserSkill userSkill;
        Skill skill;

        if(!isSkillRegistrated(addSkillRequest.getTitle())) {
            skill = new Skill(addSkillRequest.getTitle());
            skillRepository.save(skill);
        } else
        {
            skill = skillRepository.findByTitle(addSkillRequest.getTitle());
        }


        if (!isUserSkillAlreadyExists(userID, skill)) {

            userSkill = new UserSkill(userID, skill);
            userSkillRepository.save(userSkill);

        } else {
            throw new SkillAlreadyExistsException();
        }
        return userSkill;
    }

    public Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest) throws SkillNotFoundException {

        Skill skill = skillRepository.findByTitle(removeSkillRequest.getTitle());

        if(isUserSkillAlreadyExists(userID, skill)) {

            UserSkill userSkill = userSkillRepository.findByUserIDAndSkill(userID, skill);
            userSkillRepository.delete(userSkill);

        } else {
            throw new SkillNotFoundException();
        }

        return skill;
    }

    private boolean isUserSkillAlreadyExists(Long userID, Skill skill) {
        return Optional.ofNullable(userSkillRepository.findByUserIDAndSkill(userID, skill)).isPresent();
    }

    private boolean isSkillRegistrated(String title) {
        return Optional.ofNullable(skillRepository.findByTitle(title)).isPresent();
    }

}
