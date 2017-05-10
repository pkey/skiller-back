package lt.swedbank.services.skill;


import lt.swedbank.beans.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserRepository;
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

        System.out.println("----------" + skill.getId());
        skillRepository.save(skill);

        return skill;
    }
}
