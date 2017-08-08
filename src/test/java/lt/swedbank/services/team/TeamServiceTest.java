package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.team.AddTeamRequest;
import lt.swedbank.beans.request.team.UpdateTeamRequest;
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
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.skill.SkillTemplateService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import lt.swedbank.services.valueStream.ValueStreamService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;


public class TeamServiceTest {
    @Spy
    @InjectMocks
    private TeamService teamService;
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
    @Mock
    private SkillTemplateRepository skillTemplateRepository;
    @Mock
    private ValueStreamService valueStreamService;
    @Mock
    private SkillService skillService;


    private List<Team> teams;
    private Team testTeam;
    private SkillTemplate testSkillTemplate;
    private List<Skill> testSkills;
    private List<TeamSkillTemplateResponse> teamSkillTemplateResponse;
    private List<User> users;
    private UserSkillLevel userSkillLevel;
    private UserSkillResponse userSkillResponse;
    private List<UserSkillResponse> userSkillsResponse;
    private UpdateTeamRequest updateTeamRequest;
    private Team testTeam1;
    private List<Long> userIds;
    private List<Long> skillIds;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        teams = TestHelper.fetchTeams(2);
        testTeam = teams.get(0);
        users = TestHelper.fetchUsers(3);
        testSkills = TestHelper.skills.subList(0, 2);

        userSkillLevel = new UserSkillLevel();
        SkillLevel skillLevel = new SkillLevel();
        skillLevel.setLevel(2L);
        userSkillLevel.setSkillLevel(skillLevel);

        testSkillTemplate = new SkillTemplate();
        testSkillTemplate.setTeam(testTeam);
        testSkillTemplate.setSkills(testSkills);

        testTeam.setSkillTemplate(testSkillTemplate);

        teamSkillTemplateResponse = new LinkedList<>();
        teamSkillTemplateResponse.add(new TeamSkillTemplateResponse(new Skill("test"), 2, 2));

        userSkillsResponse = new ArrayList<>();
        userSkillResponse = new UserSkillResponse(new Skill("Java"));
        userSkillsResponse.add(userSkillResponse);
        userSkillResponse = new UserSkillResponse(new Skill("Testing"));
        userSkillsResponse.add(userSkillResponse);

        testTeam1 = teams.get(1);
        userIds = new ArrayList<>();
        skillIds = new ArrayList<>();
        skillIds.add(1L);
        skillIds.add(2L);
        skillIds.add(3L);
        testTeam1.setValueStream(TestHelper.fetchValueStreams().get(0));
        testTeam1.getUsers().stream().forEach(user -> userIds.add(user.getId()));
        updateTeamRequest = new UpdateTeamRequest();
        updateTeamRequest.setName(testTeam1.getName());
        updateTeamRequest.setUserIds(userIds);
        updateTeamRequest.setDepartmentId(testTeam1.getDepartment().getId());
        updateTeamRequest.setSkillIds(skillIds);
        updateTeamRequest.setStreamId(testTeam1.getValueStream().get().getId());
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
    public void getTeamSkillTemplateResponseList() throws Exception {
        doReturn(2).when(teamService).getSkillCountInTeam(any(Team.class), any(Skill.class));
        doReturn(2.0).when(teamService).getAverageSkillLevelInTeam(any(Team.class), any(Skill.class));

        Optional<SkillTemplate> skillTemplateOptional = Optional.ofNullable(testSkillTemplate);
        Mockito.when(skillTemplateService.getByTeamId(testTeam.getId())).thenReturn(skillTemplateOptional);

        List<TeamSkillTemplateResponse> responses = teamService.getTeamSkillTemplateResponseList(testTeam);

        Assert.assertEquals(responses.size(), 2);
        Assert.assertEquals(testSkillTemplate.getSkills().get(0).getTitle(),
                responses.get(0).getSkill().getTitle());
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
        //Return empty arrays to simplify testing
        doReturn(new ArrayList<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
        doReturn(new ArrayList<>()).when(teamService).getUserWithSkillResponseList(any());

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
        User userWithoutTeam = new User();
        userWithoutTeam.setId(1L);
        userWithoutTeam.setTeam(null);

        //Return empty arrays to simplify testing
        doReturn(new ArrayList<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
        doReturn(new ArrayList<>()).when(teamService).getUserWithSkillResponseList(any());


        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);
        Mockito.when(userService.getUserById(any())).thenReturn(userWithoutTeam);

        testTeam.setUsers(TestHelper.fetchUsers(5));
        testTeam.setDepartment(TestHelper.fetchDepartments(1).get(0));

        TeamResponse resultResponse = teamService.getTeamOverview(testTeam.getId(), userWithoutTeam.getId());
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

            user.addUserSkill(testUserSkill);
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

        Assert.assertEquals((teamService.getSkillCountInTeam(testTeam, testSkill) > 0), true);
    }

    @Test
    public void addNewTeam() throws Exception {
        Mockito.when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        Mockito.when(teamRepository.findByName(any())).thenReturn(null);
        Mockito.when(departmentService.getDepartmentById(testTeam.getDepartment().getId())).thenReturn(testTeam.getDepartment());
        Mockito.when(skillTemplateRepository.save(testTeam.getSkillTemplate())).thenReturn(testTeam.getSkillTemplate());

        //Return empty arrays to simplify testing
        doReturn(new ArrayList<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
        doReturn(new ArrayList<>()).when(teamService).getUserWithSkillResponseList(any());


        AddTeamRequest addTeamRequest = new AddTeamRequest();
        addTeamRequest.setName(testTeam.getName());
        addTeamRequest.setDepartmentId(testTeam.getDepartment().getId());

        TeamWithUsersResponse response = teamService.addTeam(addTeamRequest);

        Assert.assertEquals(response.getName(), testTeam.getName());
        Assert.assertEquals(response.getDepartment().getId(), testTeam.getDepartment().getId());
    }

    @Test
    public void getAllTeamOfColleaguesOverviewResponses() throws Exception {
        teams.forEach(t -> t.setSkillTemplate(new SkillTemplate(t, testSkills)));
        Mockito.when(teamRepository.findAll()).thenReturn(teams);

        //Return empty arrays to simplify testing
        doReturn(new ArrayList<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
        doReturn(new ArrayList<>()).when(teamService).getUserWithSkillResponseList(any());

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
        User myUser = users.get(0);
        Team myTeam = myUser.getTeam().orElseThrow(() -> new Exception("Test failed, user hasn't got a team"));

        Mockito.when(userService.getUserById(any())).thenReturn(myUser);

        //Return empty arrays to simplify testing
        doReturn(new ArrayList<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
        doReturn(new ArrayList<>()).when(teamService).getUserWithSkillResponseList(any());
        TeamWithUsersResponse resultTeam = teamService.getMyTeam(myUser.getId());

        Assert.assertEquals(resultTeam.getName(), testTeam.getName());
        Assert.assertEquals(resultTeam.getDepartment().getName(), testTeam.getDepartment().getName());
        Assert.assertEquals(resultTeam.getUsers().size(), 0);

    }

    @Test
    public void updateTeamTest() {
        doNothing().when(userService).updateUsersTeam(any(), any());
        doReturn(testTeam).when(teamService).getTeamById(any());
        Mockito.when(userService.getUsersByIds(any())).thenReturn(testTeam1.getUsers());
        Mockito.when(departmentService.getDepartmentById(any())).thenReturn(testTeam.getDepartment());
        Mockito.when(skillTemplateService.createOrUpdateSkillTemplate(any(), any())).thenReturn(testTeam1.getSkillTemplate());
        Mockito.when(valueStreamService.getValueStreamById(any())).thenReturn(testTeam1.getValueStream().get());
        Mockito.when(teamRepository.save(testTeam)).thenReturn(testTeam1);
        doReturn(testTeam1.getUsers()).when(teamService).getUserWithSkillResponseList(any());
        doReturn(new ArrayList<>()).when(teamService).getTeamSkillTemplateResponseList(any());
        doReturn(null).when(teamService).getTeamSkillTemplateResponseList(any());

        TeamWithUsersResponse teamWithUsersResponseResult = teamService.updateTeam(1L, updateTeamRequest);

        Assert.assertEquals(teamWithUsersResponseResult.getDepartment().getName(), testTeam1.getDepartment().getName());
        Assert.assertEquals(teamWithUsersResponseResult.getName(), testTeam1.getName());
        Assert.assertEquals(teamWithUsersResponseResult.getValueStream().getName(), testTeam1.getValueStream().get().getName());
        Assert.assertEquals(teamWithUsersResponseResult.getUsers(), testTeam1.getUsers());
    }


}