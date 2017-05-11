package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService implements ISkillService {

    @Autowired
    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill addSkill(Long userID, AddSkillRequest addSkillRequest) {

        Skill skill = new Skill(addSkillRequest.getTitle(), userID);

        skillRepository.save(skill);

        return skill;
    }

    @Override
    public Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest)
    {
        Skill skill = skillRepository.findByTitleAndUserID(removeSkillRequest.getTitle(), userID);
        skillRepository.delete(skill);
        return skill;
    }
}
