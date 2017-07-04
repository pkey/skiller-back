package lt.swedbank.services.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.user.NonColleagueResponse;
import lt.swedbank.beans.response.user.SearchUserResponse;
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
import java.util.LinkedList;
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

    public List<SearchUserResponse> searchColleagues(Long userId, String query) {
        User currentUser = getUserById(userId);

        List<SearchUserResponse> quariedUsers = this.searchUsers(query);
        List<SearchUserResponse> filteredUsers = new ArrayList<>();

        quariedUsers.remove(currentUser);

//        for (User queriedUser : quariedUsers
//             ) {
//            filteredUsers.add(getUserResponseBasedOnDepartment(currentUser, queriedUser));
//        }
        return quariedUsers;
    }

    public List<UserResponse> getNonColleagueUserResponse(List<User> users)
    {
        List userResponse = new LinkedList();
        for (User user: users
             ) {
            userResponse.add(new NonColleagueResponse(user));
        }
        return userResponse;
    }


    public List<SearchUserResponse> searchUsers(String query) {

        List<SearchUserResponse> userList = userSearchRepository.search(query);

        userList.sort(new Comparator<SearchUserResponse>() {
            @Override
            public int compare(SearchUserResponse o1, SearchUserResponse o2) {
                if (o1.getName().compareTo(o2.getName()) == 0){
                    return o1.getLastname().compareTo(o2.getLastname());
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

    private UserResponse getUserResponseBasedOnDepartment(User currentUser, User requiredUser) {

        if(requiredUser.getDepartment() != null && currentUser.getDepartment() != null) {
            if(usersInSameDepartment(currentUser, requiredUser))
            {
                return new UserEntityResponse(requiredUser);
            }
        }
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
        //If users has not assigned any team, should mean they are colleagues
        return currentUser.getDepartment().getId().equals(colleague.getDepartment().getId());
    }

}
