package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.response.user.NonColleagueResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
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
import java.util.stream.Collectors;


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

    public List<User> getUsersByIds(List<Long> ids) {
        assert ids != null;

        List<User> users = new ArrayList<>();
        for (Long id : ids) {
            users.add(getUserById(id));
        }

        return users;
    }

    public List<UserResponse> searchColleagues(Long userId, String query) {
        User currentUser = getUserById(userId);

        return this.searchUsers(query).stream().filter(u -> !currentUser.getId().equals(u.getId()))
                .map(u -> getUserResponseBasedOnDepartment(currentUser, u)).collect(Collectors.toList());
    }

    public List<User> searchUsers(String query) {

        List<User> userList = new ArrayList<>(userSearchRepository.search(query));

        userList.sort(Comparator.comparing(User::toString));

        return userList;
    }

    public Iterable<User> getAllByTeam(Team team){
        return userRepository.findAllByTeam(team);
    }

    public UserResponse getUserProfile(Long requiredUserId, String currentUserAuthId) {
        User currentUser = getUserByAuthId(currentUserAuthId);
        User requiredUser = getUserById(requiredUserId);

        return getUserResponseBasedOnDepartment(currentUser, requiredUser);
    }

    public UserResponse getMyProfile(String currentUserAuthId) {
        User currentUser = getUserByAuthId(currentUserAuthId);

        return new UserWithSkillsResponse(currentUser, userSkillService.getProfileUserSkills(currentUser.getUserSkills()));
    }

    private UserResponse getUserResponseBasedOnDepartment(User currentUser, User requiredUser) {

        if(requiredUser.getDepartment() != null && currentUser.getDepartment() != null) {
            if(usersInSameDepartment(currentUser, requiredUser))
            {
                return new UserWithSkillsResponse(requiredUser, userSkillService.getNormalUserSkillResponseList(requiredUser.getUserSkills()));
            }
        }
        return new NonColleagueResponse(requiredUser, userSkillService.getNonColleagueUserSkillResponseList(requiredUser.getUserSkills()));
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
        return userRepository.save(user);
    }

    private boolean usersInSameDepartment(User currentUser, User colleague){
        //If users has not assigned any team, should mean they are colleagues
        return currentUser.getDepartment().getId().equals(colleague.getDepartment().getId());
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUsersTeam(Team team, List<User> newUsers) {
        team.getUsers().removeAll(newUsers);
        removeAllUsers(team);
        addUsers(team, newUsers);
    }

    private void removeAllUsers(Team team) {
        team.getUsers().forEach(user -> user.setTeam(null));
        userRepository.save(team.getUsers());
    }

    private void addUsers(Team team, List<User> newUsers) {
        newUsers.forEach(newUser -> newUser.setTeam(team));
        userRepository.save(newUsers);
    }

    public List<UserResponse> getAllUserResponses() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(UserResponse::new).collect(Collectors.toList());
    }
}
