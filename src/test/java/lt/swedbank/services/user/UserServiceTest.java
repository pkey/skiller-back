package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.team.TeamService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private SkillService skillService;

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
    public void add_skill_to_user_success() {

        Mockito.when(skillService.addSkill(anyLong(),any())).thenReturn(testUserSkill);
        doReturn(testUser).when(userService).getUserById(any());

        UserSkill newUserSkill = userService.addUserSkill(anyLong(), any());
        assertEquals(testSkill.getTitle(), newUserSkill.getTitle());

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(skillService, times(1)).addSkill(anyLong(), any());
    }

    @Test
    public void set_team_to_user_success()
    {
        Mockito.when(teamService.getTeamById(any())).thenReturn(testTeam);
        doReturn(testUser).when(userService).getUserById(any());

        User newUser = userService.assignTeam(testUser.getId(), testAssignTeamRequest);
        assertEquals(testTeam, newUser.getTeam());
    }

}
