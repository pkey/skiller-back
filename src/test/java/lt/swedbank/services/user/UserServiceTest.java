package lt.swedbank.services.user;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.response.user.NonColleagueResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.repositories.search.UserSearchRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.team.TeamService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    private AssignTeamRequest testAssignTeamRequest;
    private Team testTeam;
    private UserSkill testUserSkill;
    private Skill testSkill;
    private UserSkillLevel testUserSkillLevel;
    private AddSkillRequest testAddSkillRequest;

    private List<User> testUserList;
    private User testUser;
    private User loggedUser;
    private List<Team> teams;

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private UserSkillService userSkillService;

    @Mock
    private TeamService teamService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSearchRepository userSearchRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Long userID = Long.parseLong("0");
        Long teamID = Long.parseLong("1");

        testUserList = TestHelper.fetchUsers(10);
        testUser = testUserList.get(0);
        loggedUser = testUserList.get(1);

        teams = TestHelper.fetchTeams(3);


        testUser.setTeam(teams.get(0));
        loggedUser.setTeam(teams.get(1));

        testUserSkill = new UserSkill();
        testUserSkill.setId(Integer.toUnsignedLong(0));
        testUserSkill.setUser(testUser);
        testUserSkill.setSkill(TestHelper.skills.get(6));
        testUserSkill.addUserSkillLevel(TestHelper.createUserSkillLevel(testUserSkill, TestHelper.skillLevels.get(0)));

        testAddSkillRequest = new AddSkillRequest(testUserSkill);
        testAssignTeamRequest = new AssignTeamRequest();
        testAssignTeamRequest.setTeamId(teamID);
        testAssignTeamRequest.setUserId(userID);
        testTeam = new Team();
        testTeam.setId(1L);
    }

    @After
    public void tearDown() throws Exception {
    }



    @Test(expected = UserNotFoundException.class)
    public void assignUserSkillLevelExceptionTest() throws Exception {
        doReturn(null).when(userService).getUserById(any());
        userService.assignUserSkillLevel(any(), any());
    }

    @Test
    public void getUserByAuthId() throws Exception {
        Mockito.when(userRepository.findByAuthId(any())).thenReturn(testUser);
        User resultUser = userService.getUserByAuthId("auth0id");
        assertEquals(testUser, resultUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void getUserByAuthIdExceptionTest() throws Exception {
        Mockito.when(userRepository.findByAuthId(any())).thenReturn(null);
        userService.getUserByAuthId(any());
    }

    @Test
    public void set_team_to_user_success() throws Exception {
        Mockito.when(teamService.getTeamById(any())).thenReturn(testTeam);
        doReturn(testUser).when(userService).getUserById(any());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(testUser);
        User newUser = userService.assignTeam(testUser.getId(), testAssignTeamRequest);
        assertEquals(newUser.getTeam().isPresent(), true);
        assertEquals(testTeam.getId(), newUser.getTeam().get().getId());
    }

    @Test
    public void getUserNonColleagueProfile() throws Exception {

        doReturn(testUser).when(userService).getUserById(any());
        doReturn(loggedUser).when(userService).getUserByAuthId(any());

        UserResponse testEntity = new UserResponse(testUser);
        UserResponse resultEntity = userService.getUserProfile(any(), any());


        assertEquals(resultEntity.getEmail(), testEntity.getEmail());
        assertThat(resultEntity, instanceOf(NonColleagueResponse.class));
    }

    @Test
    public void getUserColleagueProfile() throws Exception {
        User colleagueUser = testUser;
        colleagueUser.setTeam(loggedUser.getTeam().orElseThrow(() -> new Exception("Logged user does not have a team")));

        doReturn(colleagueUser).when(userService).getUserById(any());
        doReturn(loggedUser).when(userService).getUserByAuthId(any());

        UserResponse testEntity = new UserResponse(colleagueUser);
        UserResponse resultEntity = userService.getUserProfile(any(), any());

        assertEquals(resultEntity.getEmail(), testEntity.getEmail());
        assertThat(resultEntity, instanceOf(UserWithSkillsResponse.class));

    }

    @Test
    public void assignUserSkillLevelTest() throws Exception {

        doReturn(testUser).when(userService).getUserById(any());
        Mockito.when(userSkillService.assignSkillLevel(any(), any())).thenReturn(testUserSkill);

        assertEquals(userService.assignUserSkillLevel(any(), any()), testUserSkill);
    }


    @Test
    public void getUserById() throws Exception {
        Mockito.when(userRepository.findOne(any())).thenReturn(testUser);

        assertEquals(testUser, userService.getUserById(any()));
    }

    @Test(expected = UserNotFoundException.class)
    public void getUserByIdExceptionTest() {
        Mockito.when(userRepository.findOne(any())).thenReturn(null);

        userService.getUserById(any());
    }

    @Test
    public void searchUsers() throws Exception {

        Mockito.when(userSearchRepository.search(anyString())).thenReturn(new HashSet<>(testUserList));

        List<User> resultList = userService.searchUsers("");

        Assert.assertEquals(testUserList.size(), resultList.size());

    }

    @Test
    public void searchColleagues() throws Exception {

        Mockito.when(userRepository.findOne(testUser.getId())).thenReturn(testUser);

        Mockito.when(userSearchRepository.search(anyString())).thenReturn(new HashSet<>(testUserList));

        List<UserResponse> resultList = userService.searchColleagues(testUser.getId(), "");

        Assert.assertEquals(testUserList.size() - 1, resultList.size());

    }

    @Test
    public void canGetAllUsers() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(testUserList);

        List<UserResponse> userResponses = userService.getAllUserResponses();

        assertEquals(testUserList.size(), userResponses.size());

        int i = 0;
        for (UserResponse userResponse : userResponses) {
            assertEquals(userResponse.getId(), testUserList.get(i).getId());
            i++;
        }
    }
}
