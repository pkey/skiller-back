package lt.swedbank.services.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.user.NonColleagueResponse;
import lt.swedbank.beans.response.user.UserEntityResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.repositories.search.UserSearchRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSkillService userSkillService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserSearchRepository userSearchRepository;


    public User getUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public User getUserByAuthId(String authId) throws UserNotFoundException {
        User user = userRepository.findByAuthId(authId);

        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public Iterable<User> getColleagues(Long userId){
         return userRepository.findAllByIdIsNotOrderByNameAscLastNameAsc(userId);
    }

    public List<UserResponse> searchColleagues(Long userId, String query) {
        User currentUser = getUserById(userId);

        List<User> quariedUsers = this.searchUsers(query);
        List<UserResponse> filteredUsers = new ArrayList<>();

        quariedUsers.remove(currentUser);

        for (User queriedUser : quariedUsers
             ) {
            filteredUsers.add(getUserResponseBasedOnDepartment(currentUser, queriedUser));
        }

        return filteredUsers;
    }

    public List<User> searchUsers(String query) {

        List<User> userList = new ArrayList<>(userSearchRepository.search(query));


        userList.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getName().compareTo(o2.getName()) == 0){
                    return o1.getLastName().compareTo(o2.getLastName());
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });

       return userList;
    }




    public UserResponse getUserProfile(Long requeredUserId, String currentUserAuthId) {
        User currentUser = getUserByAuthId(currentUserAuthId);
        User requeredUser = getUserById(requeredUserId);

        return getUserResponseBasedOnDepartment(currentUser, requeredUser);
    }

    public UserResponse getUserResponseBasedOnDepartment(User currentUser, User requiredUser){
        if(usersInSameDepartment(currentUser, requiredUser))
            return new UserEntityResponse(requiredUser);
        else
            return new NonColleagueResponse(requiredUser);
    }

    public User addUserSkill(Long userId, AddSkillRequest addSkillRequest) throws UserNotFoundException {
        User user = getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        user.setUserSkill(userSkillService.addUserSkill(user, addSkillRequest));

        return user;
    }

    public User removeUserSkill(Long userid, RemoveSkillRequest removeSkillRequest) throws UserNotFoundException {
        User user = getUserById(userid);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<UserSkill> userSkills = user.getUserSkills();
        userSkills.remove(userSkillService.removeUserSkill(userid, removeSkillRequest));

        user.setUserSkills(userSkills);

        return user;
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

    private boolean usersInSameDepartment(User currentUser, User colleague){
        return currentUser.getDepartment().getId().equals(colleague.getDepartment().getId());
    }

}
