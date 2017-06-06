package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.team.TeamService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    private AssignTeamRequest testAssignTeamRequest;
    private Team testTeam;
    private User testUser;
    private UserSkill testUserSkill;
    private Skill testSkill;
    private AddSkillRequest testAddSkillRequest;
    private ArrayList<User> testUserList;

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private UserSkillService skillService;

    @Mock
    private TeamService teamService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Long userID = Long.parseLong("0");
        Long teamID = Long.parseLong("1");

        this.testUser = new User();
        testUser.setId(userID);
        testUser.setName("Testas");
        testUser.setLastName("Testauskas");
        testUser.setEmail("test@test.com");
        testUser.setAuthId("auth0id");

        testTeam = new Team();
        testTeam.setName("Testing Team");
        testTeam.setId(teamID);

        testSkill = new Skill("testing");
        testSkill.setId(userID);

        testUserSkill = new UserSkill(testUser, testSkill);

        testAddSkillRequest = new AddSkillRequest(testUserSkill);

        testAssignTeamRequest = new AssignTeamRequest();
        testAssignTeamRequest.setTeamId(teamID);
        testAssignTeamRequest.setUserId(userID);

        testUserList = new ArrayList<User>();
        testUserList.add(testUser);

    }

    @After
    public void tearDown() throws Exception {
    }



    @Test(expected = UserNotFoundException.class)
    public void throws_use_does_not_exist_error() throws Exception{
        Mockito.when(userRepository.findByEmail(any())).thenReturn(null);
        User resultUser = userService.getUserByEmail("something");
    }


    @Test
    public void getUserByEmail() throws Exception {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(testUser);
        User resultUser = userService.getUserByEmail("something");
        assertEquals(testUser.getEmail(), resultUser.getEmail());
    }

    @Test
    public void getUserByAuthId() throws Exception {
        Mockito.when(userRepository.findByAuthId(any())).thenReturn(testUser);
        User resultUser = userService.getUserByAuthId("auth0id");
        assertEquals(testUser, resultUser);
    }

    @Test
    public void add_skill_to_user_success() {
        Mockito.when(skillService.addUserSkill(any(), any())).thenReturn(testUserSkill);
        doReturn(testUser).when(userService).getUserById(any());

        UserSkill newUserSkill = userService.addUserSkill(anyLong(), any());
        assertEquals(testSkill.getTitle(), newUserSkill.getTitle());

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(skillService, times(1)).addUserSkill(any(), any());
    }

    @Test
    public void getUserEntityResponseList() {

        Mockito.when(userService.getSortedUsers()).thenReturn(testUserList);
        ArrayList<UserEntityResponse> resultList = new ArrayList<>();
        resultList.add(new UserEntityResponse(testUser));
        ArrayList<UserEntityResponse> testList = (ArrayList<UserEntityResponse>) userService.getUserEntityResponseList();
        assertEquals(testList.get(0).getEmail(), resultList.get(0).getEmail());
    }

    @Test
    public void set_team_to_user_success()
    {
        Mockito.when(teamService.getTeamById(any())).thenReturn(testTeam);
        doReturn(testUser).when(userService).getUserById(any());

        User newUser = userService.assignTeam(testUser.getId(), testAssignTeamRequest);
        assertEquals(testTeam, newUser.getTeam());
    }

    @Test
    public void getUserProfile() {
        doReturn(testUser).when(userService).getUserById(any());

        UserEntityResponse resultEntity = new UserEntityResponse(testUser);
        UserEntityResponse testEntity = userService.getUserProfile(any());
        assertEquals(resultEntity.getEmail(), testEntity.getEmail());
    }

    @Test
    public 

}
