package lt.swedbank.services.user;

import lt.swedbank.beans.Skill;
import lt.swedbank.beans.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;
    //@Autowired
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
     * Function returns a sure by an email
     *
     * @param email
     * @return
     */
    public User getUserByEmail(String email){
        //if (!Optional.ofNullable(userRepository.findByEmail(email)).isPresent()) {
            // throwinam error'a;
        //}

        return userRepository.findByEmail(email);
    }

    @Override
    public Skill addUserSkill(String email, AddSkillRequest addSkillRequest) {

        Long userID = getUserByEmail(email).getId();

        return skillService.addSkill(userID, addSkillRequest);
    }
}
