package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.exceptions.skill.SkillNotFaoundException;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillService skillService;

    /**
     *
     * Function returns a user by an email
     *
     * @param email - email of a user that should be found
     * @return found user
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return userRepository.findByEmail(email);
    }

    /**
     *
     * Function adds a skill to a user that is found by email
     *
     * @param email - email of a user the skill should be added ti
     * @param addSkillRequest - data of the skill that should be added
     * @return the added skill
     */
    public Skill addUserSkill(String email, AddSkillRequest addSkillRequest) {

        Long userID = getUserByEmail(email).getId();

        return skillService.addSkill(userID, addSkillRequest);
    }

    public Skill removeUserSkill(String email, RemoveSkillRequest removeSkillRequest) throws SkillNotFaoundException {

        Long userID = getUserByEmail(email).getId();
        return skillService.removeSkill(userID, removeSkillRequest);
    }
}
