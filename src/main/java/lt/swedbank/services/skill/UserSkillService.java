package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.enums.Status;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.userSkill.NonColleagueUserSkillResponse;
import lt.swedbank.beans.response.userSkill.NormalUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.userSkill.UserSkillNotFoundException;
import lt.swedbank.exceptions.userSkillLevel.UserSkillLevelIsPendingException;
import lt.swedbank.repositories.UserSkillRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserSkillService {

    @Autowired
    private UserSkillRepository userSkillRepository;
    @Autowired
    private SkillService skillService;
    @Autowired
    private UserSkillLevelService userSkillLevelService;
    @Autowired
    private UserService userService;

    public UserSkill getUserSkillByUserIdAndSkillId(Long userId, Long skillId) throws UserSkillNotFoundException {

        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId);
        if (userSkill == null) {
            throw new UserSkillNotFoundException();
        }
        return userSkill;
    }


    public UserSkillResponse addUserSkill(Long userId, AddSkillRequest addSkillRequest) throws SkillAlreadyExistsException {

        Skill skill;

        try {
            skill = skillService.findByTitle(addSkillRequest.getTitle());
        } catch (SkillNotFoundException e) {
            skill = skillService.addSkill(addSkillRequest);
        }

        if (userSkillAlreadyExists(userId, skill)) {
            throw new SkillAlreadyExistsException();
        }

        UserSkill userSkill = new UserSkill(userService.getUserById(userId), skill);
        userSkillRepository.save(userSkill);

        List<UserSkillLevel> userSkillLevels = new ArrayList<>();
        userSkillLevels.add(userSkillLevelService.addDefaultUserSkillLevel(userSkill));

        userSkill.setUserSkillLevels(userSkillLevels);

        return new UserSkillResponse(userSkill.getSkill());
    }

    public UserSkillResponse removeUserSkill(Long userId, RemoveSkillRequest removeSkillRequest) throws SkillNotFoundException, UserSkillLevelIsPendingException {

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
        return new UserSkillResponse(userSkill.getSkill());
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

    public void sortUserSkillLevels(UserSkill userSkill) {
        userSkill.getUserSkillLevels().sort((o1, o2) -> o2.getValidFrom().compareTo(o1.getValidFrom()));
    }

    public UserSkillLevel getCurrentSkillLevelStatus(UserSkill userSkill) {
        sortUserSkillLevels(userSkill);
        Collections.reverse(userSkill.getUserSkillLevels());
        Iterator<UserSkillLevel> userSkillLevelIterator = userSkill.getUserSkillLevels().iterator();
        UserSkillLevel currentUserSkillLevel = new UserSkillLevel();
        while( userSkillLevelIterator.hasNext()) {
            UserSkillLevel userSkillLevel = userSkillLevelIterator.next();
            if(userSkillLevel.getStatus() != Status.DISAPPROVED) {
                currentUserSkillLevel = userSkillLevel;
            }
        }
        return currentUserSkillLevel;
    }

    public UserSkillLevel getCurrentSkillLevel(UserSkill userSkill) {
        sortUserSkillLevels(userSkill);
        UserSkillLevel currentUserSkillLevel = null;
        for (UserSkillLevel level : userSkill.getUserSkillLevels()
                ) {
            if (level.getStatus() == Status.APPROVED) {
                currentUserSkillLevel = level;
            }
        }
        return currentUserSkillLevel;
    }

    public List<UserSkillResponse> getProfileUserSkills(List<UserSkill> userSkills)
    {
        return userSkills.stream().map(userSkill -> new NormalUserSkillResponse(userSkill.getSkill(), getCurrentSkillLevelStatus(userSkill))).collect(Collectors.toList());
    }

    public List<UserSkillResponse> getNormalUserSkillResponseList(List<UserSkill> userSkills)
    {
        return userSkills.stream().map(userSkill -> new NormalUserSkillResponse(userSkill.getSkill(), getCurrentSkillLevel(userSkill))).collect(Collectors.toList());
    }

    public List<NonColleagueUserSkillResponse> getNonColleagueUserSkillResponseList(List<UserSkill> userSkills)
    {
        return userSkills.stream().map(userSkill -> new NonColleagueUserSkillResponse(userSkill.getSkill(), getCurrentSkillLevel(userSkill))).collect(Collectors.toList());
    }

    private boolean userSkillAlreadyExists(Long userID, Skill skill) {
        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userID, skill.getId());
        return Optional.ofNullable(userSkillRepository.findByUserIdAndSkillId(userID, skill.getId())).isPresent();
    }


}
