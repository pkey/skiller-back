package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.repositories.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;
    @Autowired
    private UserRepository userRepository;

    public SkillService(SkillRepository skillRepository, UserSkillRepository userSkillRepository) {
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }

    public UserSkill addSkill(Long userID, AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        Skill skill = skillRepository.findByTitle(addSkillRequest.getTitle());

        User user = userRepository.findOne(userID);

        if(skill == null) {
            skill = new Skill(addSkillRequest.getTitle());
            skillRepository.save(skill);
        } else {
            skill = skillRepository.findByTitle(addSkillRequest.getTitle());
        }

        if(isUserSkillAlreadyExists(userID, skill)) {
            throw new SkillAlreadyExistsException();
        }


        UserSkill userSkill = new UserSkill(user, skill);
        userSkillRepository.save(userSkill);

        return userSkill;
    }

    public UserSkill removeSkill(Long userID, Skill skill) throws SkillNotFoundException {

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkill(userID, skill);

        if(userSkill == null){
            throw new SkillNotFoundException();
        }

        userSkillRepository.delete(userSkill);

        return userSkill;
    }

    private boolean isUserSkillAlreadyExists(Long userID, Skill skill) {
        return Optional.ofNullable(userSkillRepository.findByUserIdAndSkill(userID, skill)).isPresent();
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
