package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.userSkill.UserSkillNotFoundException;
import lt.swedbank.exceptions.userSkillLevel.UserSkillLevelIsPendingException;
import lt.swedbank.repositories.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSkillService {

    @Autowired
    private UserSkillRepository userSkillRepository;
    @Autowired
    private SkillService skillService;
    @Autowired
    private UserSkillLevelService userSkillLevelService;

    public UserSkill getUserSkillByUserIdAndSkillId(Long userId, Long skillId) throws UserSkillNotFoundException {

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId);
        if (userSkill == null) {
            throw new UserSkillNotFoundException();
        }
        return userSkill;
    }


    public UserSkill addUserSkill(User user, AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        Skill skill;

        try {
            skill = skillService.findByTitle(addSkillRequest.getTitle());
        } catch (SkillNotFoundException e) {
            skill = skillService.addSkill(addSkillRequest);
        }

        if (userSkillAlreadyExists(user.getId(), skill)) {
            throw new SkillAlreadyExistsException();
        }

        UserSkill userSkill = new UserSkill(user, skill);
        userSkillRepository.save(userSkill);

        List<UserSkillLevel> userSkillLevels = new ArrayList<>();
        userSkillLevels.add(userSkillLevelService.addDefaultUserSkillLevel(userSkill));

        userSkill.setUserSkillLevels(userSkillLevels);

        return userSkill;
    }

    public UserSkill removeUserSkill(Long userId, RemoveSkillRequest removeSkillRequest) throws SkillNotFoundException, UserSkillLevelIsPendingException {

        Skill skill = skillService.findByTitle(removeSkillRequest.getTitle());

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skill.getId());

        if (userSkill == null) {
            throw new SkillNotFoundException();
        }

        if (!userSkillLevelService.isLatestUserSkillLevelPending(userId, userSkill.getSkill().getId())) {
            userSkillRepository.delete(userSkill);
        } else {
            throw new UserSkillLevelIsPendingException();
        }
        return userSkill;
    }

    public UserSkill assignSkillLevel(User user, AssignSkillLevelRequest request) {

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(user.getId(), request.getSkillId());

        userSkill.addUserSkillLevel(userSkillLevelService.addUserSkillLevel(userSkill, request));

        return userSkill;
    }

    public Iterable<UserSkill> getAllUserSkillsBySkill(Skill skill)
    {
        return userSkillRepository.findBySkill(skill);
    }


    private boolean userSkillAlreadyExists(Long userID, Skill skill) {
        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userID, skill.getId());
        return Optional.ofNullable(userSkillRepository.findByUserIdAndSkillId(userID, skill.getId())).isPresent();
    }
}
