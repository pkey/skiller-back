package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

        return user;
    }

    public User getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findOne(id);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }

    /**
     * Function returns use by authentication id
     *
     * @param authId
     * @return
     * @throws UserNotFoundException
     */
    public User getUserByAuthId(String authId) throws UserNotFoundException {
        User user = userRepository.findByAuthId(authId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }

    /**
     *
     * Function adds a user skill
     *
     * @param userId - An Id of a user
     * @param addSkillRequest - data of the skill that should be added
     * @return the added skill
     */
    public Skill addUserSkill(Long userId, AddSkillRequest addSkillRequest) {
        return skillService.addSkill(userId, addSkillRequest);
    }

    /**
     *
     * Function removes a skill
     *
     * @param userId
     * @param removeSkillRequest
     * @return
     */
    public Skill removeUserSkill(Long userId, RemoveSkillRequest removeSkillRequest) throws SkillNotFoundException {
        return skillService.removeSkill(userId, removeSkillRequest);

    }
}
