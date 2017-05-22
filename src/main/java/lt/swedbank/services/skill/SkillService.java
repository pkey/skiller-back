package lt.swedbank.services.skill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.exceptions.ApplicationException;
import lt.swedbank.exceptions.ExceptionMessage;
import lt.swedbank.handlers.ExceptionHandler;
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

    @Autowired
    private ExceptionHandler exceptionHandler;

    public SkillService(SkillRepository skillRepository, UserSkillRepository userSkillRepository) {
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }

    public UserSkill addSkill(Long userID, AddSkillRequest addSkillRequest) throws ApplicationException {

        Skill skill = skillRepository.findByTitle(addSkillRequest.getTitle());

        if(skill == null) {
            skill = new Skill(addSkillRequest.getTitle());
            skillRepository.save(skill);
        } else {
            skill = skillRepository.findByTitle(addSkillRequest.getTitle());
        }

        if(isUserSkillAlreadyExists(userID, skill)) {
            throw exceptionHandler.handleException(ExceptionMessage.SKILL_ALREADY_EXISTS);
        }

        UserSkill userSkill = new UserSkill(userID, skill);
        userSkillRepository.save(userSkill);

        return userSkill;
    }

    public UserSkill removeSkill(Long userID, Skill skill) throws ApplicationException {

        UserSkill userSkill = userSkillRepository.findByUserIDAndSkill(userID, skill);

        if(userSkill == null){
            throw exceptionHandler.handleException(ExceptionMessage.SKILL_NOT_FOUND);
        }

        userSkillRepository.delete(userSkill);

        return userSkill;
    }

    private boolean isUserSkillAlreadyExists(Long userID, Skill skill) {
        return Optional.ofNullable(userSkillRepository.findByUserIDAndSkill(userID, skill)).isPresent();
    }

}
