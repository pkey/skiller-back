package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillAlreadyAddedToUserException;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill addSkill(Long userID, AddSkillRequest addSkillRequest) throws SkillAlreadyAddedToUserException {

        Skill skill;

        if(isSkillAlreadyExists(userID, addSkillRequest.getTitle())) {

            skill = new Skill(addSkillRequest.getTitle(), userID);

            skillRepository.save(skill);
        } else {
            throw new SkillAlreadyAddedToUserException();
        }

        return skill;
    }

    public Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest)
    {
        Skill skill = skillRepository.findByTitleAndUserID(removeSkillRequest.getTitle(), userID);
        skillRepository.delete(skill);
        return skill;
    }

    public boolean isSkillAlreadyExists(Long userID, String skillTitle) {
        return skillRepository.findByTitleAndUserID(skillTitle, userID) != null;
    }
}
