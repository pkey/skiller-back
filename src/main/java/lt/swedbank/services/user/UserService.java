package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;
    private SkillService skillService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSkillService(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     *
     * Function returns a user by an email
     *
     * @param email - email of a user that should be found
     * @return found user
     */
    public User getUserByEmail(String email){

        /*
          TODO
          add custom exception throwing if user couldn't be found
        if (!Optional.ofNullable(userRepository.findByEmail(email)).isPresent()) {
            throwinam error'a;
        }*/

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
    @Override
    public Skill addUserSkill(String email, AddSkillRequest addSkillRequest) {

        Long userID = getUserByEmail(email).getId();

        return skillService.addSkill(userID, addSkillRequest);
    }
}
