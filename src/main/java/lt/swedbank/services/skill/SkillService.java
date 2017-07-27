package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillTemplate;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.SkillTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SkillTemplateRepository skillTemplateRepository;

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

    public Iterable<Skill> findALlById(List<Long> ids){
        return skillRepository.findSkillsById(ids);
    }


    public Iterable<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Iterable<SkillEntityResponse> getSkillEntityResponseList() {
        List<SkillEntityResponse> skillList = new ArrayList<>();
        for (Skill skill : this.getAllSkills()) {
            skillList.add(new SkillEntityResponse(skill));
        }
        return skillList;
    }

    public SkillTemplate createSkillTemplate (Team team, List<Skill> skills) {
        SkillTemplate skillTemplate = new SkillTemplate(team, skills);
        return skillTemplateRepository.save(skillTemplate);
    }

    public List<Skill> getSkillsByIds(List<Long> skillsId) {
        assert skillsId != null;

        List<Skill> skills = new ArrayList<>();
        for (Long id : skillsId) {
            skills.add(getSkillById(id));
        }

        return skills;
    }

    private Skill getSkillById(Long id) {
        Skill skill = skillRepository.findOne(id);
        if (skill == null) {
            throw new SkillNotFoundException();
        }
        return skill;
    }
}
