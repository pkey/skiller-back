package lt.swedbank.services.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSkillService userSkillService;
    @Autowired
    private TeamService teamService;


    public User getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return userRepository.findByEmail(email);
    }

    public User getUserByAuthId(String authId) throws UserNotFoundException {
        User user = userRepository.findByAuthId(authId);

        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Iterable<User> getSortedUsers() {
        return userRepository.findAllByOrderByNameAscLastNameAsc();
    }

    public UserEntityResponse getUserProfile(Long id) {
        return new UserEntityResponse(getUserById(id));
    }

    public Iterable<UserEntityResponse> getUserEntityResponseList() {
        List<UserEntityResponse> userList = new ArrayList<>();
        for (User user : getSortedUsers()
                ) {
            userList.add(new UserEntityResponse(user));
        }
        return userList;
    }

    public User addUserSkill(Long userId, AddSkillRequest addSkillRequest) throws UserNotFoundException {
        User user = getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        user.setUserSkill(userSkillService.addUserSkill(user, addSkillRequest));

        return user;
    }

    public UserSkill removeUserSkill(Long userid, RemoveSkillRequest removeSkillRequest) throws UserNotFoundException {
        if (getUserById(userid) == null) {
            throw new UserNotFoundException();
        }

        return userSkillService.removeUserSkill(userid, removeSkillRequest);
    }

    public UserSkill assignUserSkillLevel(Long userid, AssignSkillLevelRequest request) throws UserNotFoundException {
        User user = getUserById(userid);

        if (user == null) {
            throw new UserNotFoundException();
        }
        return userSkillService.assignSkillLevel(user, request);
    }

    public User assignTeam(final Long userId, final AssignTeamRequest assignTeamRequest) {
        User user = getUserById(userId);
        user.setTeam(teamService.getTeamById(assignTeamRequest.getTeamId()));
        userRepository.save(user);
        return user;
    }

}
