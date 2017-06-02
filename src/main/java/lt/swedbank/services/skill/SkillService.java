package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.repositories.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillLevelService skillLevelService;

    public SkillService(SkillRepository skillRepository, UserSkillRepository userSkillRepository) {
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }



    public Skill addSkill(AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {
        Skill skill = skillRepository.findByTitle(addSkillRequest.getTitle());

        if(skill == null) {
            skill = new Skill(addSkillRequest.getTitle());
            skillRepository.save(skill);
        }

        return skill;
    }

    public Skill findByTitle(String title) throws SkillNotFoundException {
        Skill skill = skillRepository.findByTitle(title);

        if(skill == null) {
            throw new SkillNotFoundException();
        }

        return skill;
    }

    public Skill findById(Long id) throws SkillNotFoundException {
        Skill skill = skillRepository.findOne(id);

        if(skill == null) {
            throw new SkillNotFoundException();
        }

        return skill;
    }




    public Iterable<Skill> getAllSkills()
    {
        return skillRepository.findAll();
    }


    public Iterable<SkillEntityResponse> getSkillEntityResponseList()
    {
        List<SkillEntityResponse> skillList = new ArrayList<>();
        for (Skill skill: getAllSkills()
             ) {
                skillList.add(new SkillEntityResponse(skill));
        }
        return skillList;
    }

}
