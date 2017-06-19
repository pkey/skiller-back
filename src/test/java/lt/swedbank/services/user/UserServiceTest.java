package lt.swedbank.services.user;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.response.user.UserEntityResponse;
import lt.swedbank.beans.response.user.UserResponse;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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

        testUserSkill = new UserSkill();
        testUserSkill.setId(Integer.toUnsignedLong(0));
        testUserSkill.setUser(testUser);
        testUserSkill.setSkill(new Skill("Test Skill"));

        testTeam = new Team();
        testTeam.setName("Testing Team");
        testTeam.setId(teamID);

        testAddSkillRequest = new AddSkillRequest(testUserSkill);

        testAssignTeamRequest = new AssignTeamRequest();
        testAssignTeamRequest.setTeamId(teamID);
        testAssignTeamRequest.setUserId(userID);


    }

    @After
    public void tearDown() throws Exception {
    }



    @Test(expected = UserNotFoundException.class)
    public void assignUserSkillLevelExceptionTest() {
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
    public void add_skill_to_user_success() {
        UserSkill testUserSkill = mock(UserSkill.class);
        Mockito.when(userSkillService.addUserSkill(any(), any())).thenReturn(testUserSkill);
        Mockito.when(userRepository.findOne(testUser.getId())).thenReturn(testUser);


        User user = userService.addUserSkill(testUser.getId(), testAddSkillRequest);
        assertEquals(user, testUser);

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(userSkillService, times(1)).addUserSkill(any(), any());
    }

    @Test(expected = UserNotFoundException.class)
    public void addUserSkillExceptionTest() {
        doReturn(null).when(userService).getUserById(any());

        userService.addUserSkill(any(), any());
    }

    @Test
    public void remove_skill_from_user() {
        Mockito.when(userSkillService.removeUserSkill(any(), any())).thenReturn(testUserSkill);
        doReturn(testUser).when(userService).getUserById(any());

        User resultUser = userService.removeUserSkill(anyLong(), any());
        assertEquals(testUser, resultUser);

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(userSkillService, times(1)).removeUserSkill(any(), any());
    }

    @Test(expected = UserNotFoundException.class)
    public void removeUserSkillExceptionTest() {
        doReturn(null).when(userService).getUserById(any());

        userService.removeUserSkill(any(), any());
    }

    @Test
    public void set_team_to_user_success() {
        Mockito.when(teamService.getTeamById(any())).thenReturn(testTeam);
        doReturn(testUser).when(userService).getUserById(any());

        User newUser = userService.assignTeam(testUser.getId(), testAssignTeamRequest);
        assertEquals(testTeam, newUser.getTeam());
    }

    @Test
    public void getUserProfile() {
        doReturn(testUser).when(userService).getUserById(any());

        UserResponse resultEntity = new UserEntityResponse(testUser);
        UserResponse testEntity = userService.getUserProfile(any(), any());
        assertEquals(resultEntity.getEmail(), testEntity.getEmail());
    }

    @Test
    public void assignUserSkillLevelTest() {

        doReturn(testUser).when(userService).getUserById(any());
        Mockito.when(userSkillService.assignSkillLevel(any(), any())).thenReturn(testUserSkill);

        assertEquals(userService.assignUserSkillLevel(any(), any()), testUserSkill);
    }


    @Test
    public void getUserById() {
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

        for (int i = 0; i < testUserList.size() - 1; i++) {
            assertNotEquals(1, compareNames(testUserList.get(i), resultList.get(i+1)));

            if(compareNames(testUserList.get(i), resultList.get(i+1)) == 0){
                assertNotEquals(1, compareLastNames(testUserList.get(i), resultList.get(i+1) ));
            }

        }

    }

    @Test
    public void searchColleagues() throws Exception {

        Mockito.when(userRepository.findOne(testUser.getId())).thenReturn(testUser);

        Mockito.when(userSearchRepository.search(anyString())).thenReturn(new HashSet<>(testUserList));

        List<UserResponse> resultList = userService.searchColleagues(testUser.getId(), "");

        Assert.assertEquals(testUserList.size() - 1, resultList.size());


    }

    private int compareNames(User actualUser, User resultUser) {
        return actualUser.getName().compareTo(resultUser.getName());
    }

    private int compareLastNames(User actualUser, User resultUser) {
        return actualUser.getLastName().compareTo(resultUser.getLastName());
    }

}
