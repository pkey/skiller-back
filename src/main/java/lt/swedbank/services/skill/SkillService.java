package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
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

        Skill skill;

        if(!isSkillAlreadyExists(userID, addSkillRequest.getTitle())) {

            skill = new Skill(addSkillRequest.getTitle(), userID);

            skillRepository.save(skill);
        } else {
            throw new SkillAlreadyExistsException();
        }

        return skill;
    }

    @Override
    public Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest) throws SkillNotFaoundException
    {
        if (!Optional.ofNullable(skillRepository.findByTitleAndUserID(removeSkillRequest.getTitle(), userID)).isPresent()) {
            throw new SkillNotFaoundException();
        }
        Skill skill = skillRepository.findByTitleAndUserID(removeSkillRequest.getTitle(), userID);
        skillRepository.delete(skill);
        return skill;
    }

    public boolean isSkillAlreadyExists(Long userID, String skillTitle) {
        return Optional.ofNullable(skillRepository.findByTitleAndUserID(skillTitle, userID)).isPresent();
    }
}
