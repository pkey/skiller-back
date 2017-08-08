package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.team.AddTeamRequest;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.beans.request.team.UpdateTeamRequest;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;
import lt.swedbank.exceptions.team.TeamNotFoundException;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.department.DepartmentService;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.skill.SkillTemplateService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.teamSkill.TeamSkillService;
import lt.swedbank.services.user.UserService;
import lt.swedbank.services.valueStream.ValueStreamService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

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
    private ValueStreamService valueStreamService;
    @Mock
    private TeamSkillService teamSkillService;
    @Mock
    private SkillService skillService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;


    private List<Team> teams;
    private Team testTeam;
    private SkillTemplate testSkillTemplate;
    private List<Skill> testSkills;
    private Skill testSkill;
    private List<SkillTemplateResponse> skillTemplateResponse;
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

        teams = TestHelper.fetchTeams();
        testTeam = teams.get(0);
        users = TestHelper.fetchUsers(3);
        testSkills = TestHelper.skills.subList(0, 2);
        testSkill = testSkills.get(0);


        userSkillLevel = new UserSkillLevel();
        SkillLevel skillLevel = new SkillLevel();
        skillLevel.setLevel(2L);
        userSkillLevel.setSkillLevel(skillLevel);

        testSkillTemplate = new SkillTemplate();
        testSkillTemplate.setTeam(testTeam);
        testSkillTemplate.addSkill(testSkill);

        skillTemplateResponse = new LinkedList<>();
        skillTemplateResponse.add(new SkillTemplateResponse(
                new SkillEntityResponse(new Skill("test")),
                2,
                (double) 2));

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
    public void get_ordered_team_skill_template_response_list() throws Exception {
        Skill testSkillWithLowerCount = new Skill("Test Skill 2");
        testSkillTemplate.addSkill(testSkillWithLowerCount);
        Optional<SkillTemplate> skillTemplateOptional = Optional.ofNullable(testSkillTemplate);
        Mockito.when(skillTemplateService.getSkillTemplateByTeamId(testTeam.getId())).thenReturn(skillTemplateOptional);


        TeamSkill teamSkillWithHigherUserCount = new TeamSkill(testTeam, testSkill, 2, 1D);
        TeamSkill teamSkillWithLowerUserCount = new TeamSkill(testTeam, testSkill, 1, 1D);

        Mockito.when(teamSkillService.getCurrentTeamSkillByTeamAndSkill(any(Team.class), any(Skill.class)))
                .thenReturn(teamSkillWithLowerUserCount,
                        teamSkillWithHigherUserCount);
        Mockito.when(skillTemplateService.getByTeamId(testTeam.getId())).thenReturn(skillTemplateOptional);

        Set<SkillTemplateResponse> responses = teamService.getTeamSkillTemplateResponseList(testTeam);

        Assert.assertEquals(2, responses.size());
        Assert.assertEquals(true, responses.contains(new SkillTemplateResponse(
                new SkillEntityResponse(testSkill), 1, 1D)));
        Assert.assertEquals(true, responses.contains(new SkillTemplateResponse(
                new SkillEntityResponse(testSkillWithLowerCount), 1, 1D)));

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
        doReturn(new TreeSet<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
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
        doReturn(new TreeSet<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
        doReturn(new ArrayList<>()).when(teamService).getUserWithSkillResponseList(any());


        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);
        Mockito.when(userService.getUserById(any())).thenReturn(userWithoutTeam);

        testTeam.setUsers(TestHelper.fetchUsers(5));
        testTeam.setDepartment(TestHelper.fetchDepartments().get(0));

        TeamResponse resultResponse = teamService.getTeamOverview(testTeam.getId(), userWithoutTeam.getId());
        Assert.assertThat(resultResponse, instanceOf(NonColleagueTeamOverviewWithUsersResponse.class));
    }


    @Test
    public void addNewTeam() throws Exception {
        Mockito.when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        Mockito.when(teamRepository.findByName(any())).thenReturn(null);
        Mockito.when(departmentService.getDepartmentById(testTeam.getDepartment().getId())).thenReturn(testTeam.getDepartment());

        //Return empty arrays to simplify testing
        doReturn(new TreeSet<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
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
        doReturn(new TreeSet<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
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
        myUser.setTeam(testTeam);
        //Team myTeam = myUser.getTeam().orElseThrow(() -> new Exception("Test failed, user hasn't got a team"));

        Mockito.when(userService.getUserById(any())).thenReturn(myUser);

        //Return empty arrays to simplify testing
        doReturn(new TreeSet<>()).when(teamService).getTeamSkillTemplateResponseList(any(Team.class));
        doReturn(new ArrayList<>()).when(teamService).getUserWithSkillResponseList(any());
        TeamWithUsersResponse resultTeam = teamService.getMyTeam(myUser.getId());

        Assert.assertEquals(resultTeam.getName(), testTeam.getName());
        Assert.assertEquals(resultTeam.getDepartment().getName(), testTeam.getDepartment().getName());
        Assert.assertEquals(0, resultTeam.getUsers().size());

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
        doReturn(new HashSet<>()).when(teamService).getTeamSkillTemplateResponseList(any());
        doReturn(null).when(teamService).getTeamSkillTemplateResponseList(any());

        TeamWithUsersResponse teamWithUsersResponseResult = teamService.updateTeam(1L, updateTeamRequest);

        Assert.assertEquals(teamWithUsersResponseResult.getDepartment().getName(), testTeam1.getDepartment().getName());
        Assert.assertEquals(teamWithUsersResponseResult.getName(), testTeam1.getName());
        Assert.assertEquals(teamWithUsersResponseResult.getValueStream().getName(), testTeam1.getValueStream().get().getName());
        Assert.assertEquals(teamWithUsersResponseResult.getUsers(), testTeam1.getUsers());
    }


}