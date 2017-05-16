package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.skill.*;
import lt.swedbank.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService implements ISkillService{

    @Autowired
    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill addSkill(Long userID, AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        Skill skill = skillRepository.findByTitleAndUserID(addSkillRequest.getTitle(), userID);

        if(skill == null) {

            skill = new Skill(addSkillRequest.getTitle(), userID);

            skillRepository.save(skill);
        } else {
            throw new SkillAlreadyExistsException();
        }

        return skill;
    }

    @Override
    public Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest) throws SkillNotFoundException {

        Skill skill = skillRepository.findByTitleAndUserID(removeSkillRequest.getTitle(), userID);

        if(skill == null){
            throw new SkillNotFoundException();
        }

        return skill;
    }
}
