package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.team.AddTeamRequest;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;
import lt.swedbank.exceptions.team.TeamNotFoundException;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.SkillTemplateRepository;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.department.DepartmentService;
import lt.swedbank.services.skill.SkillTemplateService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;


public class TeamServiceTest {
    @Spy
    @InjectMocks
    private TeamService teamService;

    @Mock
    private SkillTemplateRepository skillTemplateRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private UserSkillService userSkillService;

    @Mock
    private SkillTemplateService skillTemplateService;
    //Test DAta

    private List<Team> teams;
    private Team testTeam;
    private NonColleagueTeamOverviewWithUsersResponse nonColleagueTeamOverviewWithUsersResponse;
    private SkillTemplate skillTemplate;
    private List<Skill> skills;
    private List<TeamSkillTemplateResponse> teamSkillTemplateResponse;
    private List<User> users;
    private UserSkillLevel userSkillLevel;
    private UserSkillResponse userSkillResponse;
    private List<UserSkillResponse> userSkillsResponse;
    private TeamWithUsersResponse teamWithUsersResponse;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        teams = TestHelper.fetchTeams(2);

        testTeam = teams.get(0);
        testTeam.getDepartment().setDivision(new Division());

        users = TestHelper.fetchUsers(3);

        skills = new LinkedList<>();
        skills.add(new Skill("test"));
        skills.add(new Skill("test2"));

        userSkillLevel = new UserSkillLevel();
        SkillLevel skillLevel = new SkillLevel();
        skillLevel.setLevel(2L);
        userSkillLevel.setSkillLevel(skillLevel);

        skillTemplate = new SkillTemplate();
        skillTemplate.setTeam(testTeam);
        skillTemplate.setSkills(skills);

        teamSkillTemplateResponse = new LinkedList<>();
        teamSkillTemplateResponse.add(new TeamSkillTemplateResponse(new Skill("test"), 2, 2));

        userSkillsResponse = new ArrayList<>();
        userSkillResponse = new UserSkillResponse(new Skill("Java"));
        userSkillsResponse.add(userSkillResponse);
        userSkillResponse = new UserSkillResponse(new Skill("Testing"));
        userSkillsResponse.add(userSkillResponse);
    }

    @Test
    public void getAllTeams() throws Exception {
        Mockito.when(teamRepository.findAll()).thenReturn(teams);

        List<Team> resultTeams = (List<Team>) teamService.getAllTeams();

        Assert.assertEquals(teams, resultTeams);
    }

    @Test
    public void getTeamById() throws Exception {
        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);

        Team resultTeam = teamService.getTeamById(testTeam.getId());

        Assert.assertEquals(resultTeam, testTeam);
    }

    @Test
    public void getTeamSkillTemplate() {
        Mockito.when(skillTemplateRepository.findOneByTeamId(any())).thenReturn(skillTemplate);

        Assert.assertEquals(teamService.getTeamSkillTemplate(testTeam), skillTemplate);
    }

    @Test
    public void getTeamSkillTemplateResponseList() throws Exception {
        doReturn(2).when(teamService).getSkillCountInTeam(any(Team.class), any(Skill.class));
        doReturn(2.0).when(teamService).getAverageSkillLevelInTeam(any(Team.class), any(Skill.class));

        Mockito.when(teamService.getTeamSkillTemplate(any())).thenReturn(skillTemplate);
        Mockito.when(teamService.getTeamSkillTemplateResponseList(any())).thenReturn(teamSkillTemplateResponse);

        Assert.assertEquals(teamSkillTemplateResponse, teamService.getTeamSkillTemplateResponseList(any()));
        Assert.assertEquals(teamSkillTemplateResponse.get(0).getSkill().getTitle(),
                teamService.getTeamSkillTemplateResponseList(any()).get(0).getSkill().getTitle() );
    }

    @Test
    public void getTeamSkillTemplateResponseListException() throws Exception {
        List<TeamSkillTemplateResponse> teamSkillTemplateResponses = teamService.getTeamSkillTemplateResponseList(any());
        Assert.assertEquals(teamSkillTemplateResponses.isEmpty(), true);
    }

    @Test(expected = TeamNotFoundException.class)
    public void getTeamByIdException() throws Exception {
        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(null);
        teamService.getTeamById(testTeam.getId());
    }

    @Test
    public void getTeamOverview() throws Exception {
        User userFromSameTeam = users.get(0);

        testTeam.setUsers(TestHelper.fetchUsers(5));

        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);
        Mockito.when(userService.getUserById(userFromSameTeam.getId())).thenReturn(userFromSameTeam);

        TeamWithUsersResponse resultResponse = teamService.getTeamOverview(testTeam.getId(), userFromSameTeam.getId());

        Assert.assertNotEquals(resultResponse, null);
        Assert.assertNotEquals(resultResponse.getUsers(), null);

        Assert.assertThat(resultResponse, instanceOf(TeamResponse.class));
        Assert.assertThat(resultResponse, instanceOf(ColleagueTeamOverviewWithUsersResponse.class));

    }

    @Test
    public void getNonColleagueTeamOverview() throws Exception {
        User userFromAnotherDepartment = new User();
        userFromAnotherDepartment.setId(1L);
        userFromAnotherDepartment.setTeam(null);
        Department department = new Department();
        Team team = new Team();

        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);
        Mockito.when(userService.getUserById(any())).thenReturn(userFromAnotherDepartment);
        
        testTeam.setUsers(TestHelper.fetchUsers(5));
        team.setDepartment(department);

        TeamResponse resultResponse = teamService.getTeamOverview(testTeam.getId(), userFromAnotherDepartment.getId());
        Assert.assertThat(resultResponse, instanceOf(NonColleagueTeamOverviewWithUsersResponse.class));
    }
    
    @Test
    public void getAverageSkillLevelInTeam() {
        List<User> testUsers = TestHelper.fetchUsers(2);
        for (User testUser : testUsers) {
            testUser.setTeam(testTeam);
        }

        Skill testSkill = new Skill("Test Skill");

        //Change user skill levels from default to test better
        for (User user : testUsers) {
            List<UserSkill> userSkills;

            UserSkill testUserSkill = new UserSkill(user, testSkill);
            testUserSkill.addUserSkillLevel(TestHelper.createUserSkillLevel(testUserSkill, TestHelper.skillLevels.get(1)));

            user.setUserSkill(testUserSkill);
        }
        Mockito.when(userService.getAllByTeam(testTeam)).thenReturn(testUsers);
        Mockito.when(userSkillService.getCurrentSkillLevel(any())).thenReturn(userSkillLevel);

        Team testTeam = TestHelper.fetchTeams(1).get(0);
        testTeam.setUsers(testUsers);

        Assert.assertEquals(2L, teamService.getAverageSkillLevelInTeam(testTeam, testSkill), 0.0002);
    }

    @Test
    public void getSkillCountInTeam() {
        Mockito.when(userService.getAllByTeam(any())).thenReturn(users);

        Skill testSkill = users.get(0).getUserSkills().get(0).getSkill();

        Assert.assertEquals((teamService.getSkillCountInTeam(testTeam, testSkill) > 0 ), true);
    }

    @Test
    public void addNewTeam() throws Exception {
        Mockito.when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        Mockito.when(departmentService.getDepartmentById(testTeam.getDepartment().getId())).thenReturn(testTeam.getDepartment());


        AddTeamRequest addTeamRequest = new AddTeamRequest();
        addTeamRequest.setName(testTeam.getName());
        addTeamRequest.setDepartmentId(testTeam.getDepartment().getId());

        Assert.assertEquals(teamService.addTeam(addTeamRequest).getId(), new TeamResponse(testTeam).getId());
    }

    @Test
    public void getAllTeamOfColleaguesOverviewResponses() throws Exception {
        teams.forEach(t -> t.setSkillTemplate(new SkillTemplate(t, skills)));
        Mockito.when(teamRepository.findAll()).thenReturn(teams);

        List<TeamWithUsersResponse> responses = teamService.getAllTeamOverviewResponses();

        Assert.assertEquals(teams.size(), responses.size());
        Assert.assertEquals(responses.get(0).getId(), teams.get(0).getId());
    }

    @Test
    public void getUserWithSkillResponseList() {
        Mockito.when(userSkillService.getNormalUserSkillResponseList(any())).thenReturn(userSkillsResponse);

        List<UserWithSkillsResponse> testUserWithSkillsResponses = teamService.getUserWithSkillResponseList(TestHelper.fetchUsers(3));

        Assert.assertEquals(testUserWithSkillsResponses.isEmpty(), false);
        Assert.assertEquals(testUserWithSkillsResponses.size(), 3);
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(testUserWithSkillsResponses.get(i).getSkills().get(0).getTitle(), "Java");
            Assert.assertEquals(testUserWithSkillsResponses.get(i).getSkills().get(1).getTitle(), "Testing");
            Assert.assertEquals(testUserWithSkillsResponses.get(i).getName().isEmpty(), false);
            Assert.assertEquals(testUserWithSkillsResponses.get(i).getLastName().isEmpty(), false);
        }
    }

    @Test
    public void getMyTeam() throws Exception {
        Mockito.when(userService.getUserById(any())).thenReturn(users.get(0));

        testTeam.setUsers(users);

        TeamWithUsersResponse resultTeam = teamService.getMyTeam(users.get(0).getId());

        Assert.assertEquals(resultTeam.getName(), testTeam.getName());
        Assert.assertEquals(resultTeam.getDepartment().getName(), testTeam.getDepartment().getName());
        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(resultTeam.getUsers().get(i).getName(), testTeam.getUsers().get(i).getName());
            Assert.assertEquals(resultTeam.getUsers().get(i).getLastName(), testTeam.getUsers().get(i).getLastName());
        }
    }


}