package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.RemoveSkillRequest;
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


    @Override
    public Skill removeUserSkill(String email, RemoveSkillRequest removeSkillRequest) {

        Long userID = getUserByEmail(email).getId();

        return skillService.removeSkill(userID, removeSkillRequest);
    }


}
