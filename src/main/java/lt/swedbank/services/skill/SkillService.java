package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill addSkill(AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        if (skillRepository.findByTitle(addSkillRequest.getTitle()) != null) {
            throw new SkillAlreadyExistsException();
        }

        Skill skill = new Skill(addSkillRequest.getTitle());
        skillRepository.save(skill);

        return skill;
    }

    public Skill findByTitle(String title) throws SkillNotFoundException {
        Skill skill = skillRepository.findByTitle(title);

        if (skill == null) {
            throw new SkillNotFoundException();
        }

        return skill;
    }

    public Skill findById(Long id) throws SkillNotFoundException {
        Skill skill = skillRepository.findOne(id);

        if (skill == null) {
            throw new SkillNotFoundException();
        }

        return skill;
    }


    public Iterable<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Iterable<SkillEntityResponse> getSkillEntityResponseList() {
        List<SkillEntityResponse> skillList = new ArrayList<>();
        for (Skill skill : this.getAllSkills()
                ) {
            skillList.add(new SkillEntityResponse(skill));
        }
        return skillList;
    }

}
