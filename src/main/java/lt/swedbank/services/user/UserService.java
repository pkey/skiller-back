package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillService userSkillService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private SkillRepository skillRepository;


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
     * @param userid - email of a user the skill should be added ti
     * @param addSkillRequest - data of the skill that should be added
     * @return the added skill
     */

    public UserSkill addUserSkill(Long userid, AddSkillRequest addSkillRequest) throws UserNotFoundException {

        if (getUserById(userid) == null) {
            throw new UserNotFoundException();
            }
        return userSkillService.addSkill(userid, addSkillRequest);
    }

    public UserSkill removeUserSkill(Long userid, RemoveSkillRequest removeSkillRequest) throws UserNotFoundException {
        if (getUserById(userid) == null) {
            throw new UserNotFoundException();
        }
        Skill skill = skillRepository.findByTitle(removeSkillRequest.getTitle());
        return userSkillService.removeSkill(userid, skill);
    }

    public User getUserByAuthId(String authId) throws UserNotFoundException {
        User user = userRepository.findByAuthId(authId);

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

    public Iterable<User> getSortedUsers()
    {
        return userRepository.findAllByOrderByNameAscLastNameAsc();
    }


    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Iterable<UserEntityResponse> getUserEntityResponseList() {
        List<UserEntityResponse> userList = new ArrayList<>();
        for (User user: getSortedUsers()
                ) {
            userList.add(new UserEntityResponse(user));
        }
        return userList;
    }

    public User assignTeam(final Long userId, final AssignTeamRequest assignTeamRequest) {
        User user = getUserById(userId);
        user.setTeam(teamService.getTeamById(assignTeamRequest.getTeamId()));
        userRepository.save(user);
        return user;
    }

    public List<User> sortUsers(List<User> users)
    {
        Collections.sort(users);
        return users;
    }

    public UserEntityResponse getUserProfile(Long id) {
            return new UserEntityResponse(getUserById(id));
    }
}
