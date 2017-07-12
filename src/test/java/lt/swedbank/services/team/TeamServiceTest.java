package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.response.TeamSkillTeamplateResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.TeamOverviewResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skillTemplate.NoSkillTemplateFoundException;
import lt.swedbank.exceptions.team.TeamNotFoundException;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.SkillTemplateRepository;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.user.UserService;
import org.bouncycastle.jcajce.provider.symmetric.TEA;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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

    private List<Team> teams;

    private Team testTeam;

    private List<User> users;

    private SkillTemplate testSkillTemplate;

    private Iterable<Skill> testSkills;
    
    private List<TeamSkillTeamplateResponse> teamSkillTeamplateResponses;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        teams = TestHelper.fetchTeams(2);
        testTeam = teams.get(0);

        users = TestHelper.fetchUsers(3);

        //Assign all users same team;
        for (User user : users
             ) {
            user.setTeam(testTeam);
        }
        testSkills = new LinkedList<>();
        testSkills.add(new Skill("test"));
        testSkills.add(new Skill("test2"));

        testSkillTemplate = new SkillTemplate();
        testSkillTemplate.setTeam(testTeam);
        testSkillTemplate.setSkills(testSkills);

        teamSkillTeamplateResponses = new LinkedList<>();
        teamSkillTeamplateResponses.add(new TeamSkillTeamplateResponse(new Skill("test"), 2));

    }

    @Test
    public void getAllTeams() throws Exception {
        Mockito.when(teamRepository.findAll()).thenReturn(teams);

        List<Team> resultTeams = new ArrayList<>();
        resultTeams = (ArrayList<Team>) teamService.getAllTeams();

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

        Mockito.when(skillTemplateRepository.findOneByTeam(any())).thenReturn(testSkillTemplate);

        Assert.assertEquals(teamService.getTeamSkillTemplate(testTeam), testSkillTemplate);
    }

    @Test
    public void getTeamSkillTemplateResponseList() throws Exception {
        doReturn(2).when(teamService).getSkillCountInTeam(any(Team.class), any(Skill.class));
        Mockito.when(teamService.getTeamSkillTemplate(any())).thenReturn(testSkillTemplate);
        Mockito.when(teamService.getTeamSkillTemplateResponseList(any())).thenReturn(teamSkillTeamplateResponses);

        Assert.assertEquals(teamSkillTeamplateResponses, teamService.getTeamSkillTemplateResponseList(any()));
        Assert.assertEquals(teamSkillTeamplateResponses.get(0).getSkill().getTitle(),
                teamService.getTeamSkillTemplateResponseList(any()).get(0).getSkill().getTitle() );
    }

    @Test(expected = NoSkillTemplateFoundException.class)
    public void getTeamSkillTemplateResponseListException(){

        teamService.getTeamSkillTemplateResponseList(any());
    }

    @Test(expected = TeamNotFoundException.class)
    public void getTeamByIdException() throws Exception {
        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(null);

        teamService.getTeamById(testTeam.getId());
    }

    @Test
    public void getTeamOverview() throws Exception {
        User userFromSameTeam = users.get(0);


        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);
        Mockito.when(userService.getUserById(userFromSameTeam.getId())).thenReturn(userFromSameTeam);

        TeamOverviewResponse resultResponse = teamService.getTeamOverview(testTeam.getId(), userFromSameTeam.getId());

        Assert.assertNotEquals(resultResponse, null);
        Assert.assertNotEquals(resultResponse.getUsers(), null);

        int i = 0;
        for (UserResponse userResponse: resultResponse.getUsers()
             ) {
            Assert.assertEquals(userResponse.getId(), users.get(i).getId());
            i++;
        }

        Assert.assertThat(resultResponse, instanceOf(TeamOverviewResponse.class));
        Assert.assertThat(resultResponse, instanceOf(ColleagueTeamOverviewResponse.class));

    }

    public int getSkillCountInTeam(Team team, Skill skill)
    {
        List<User> users = (List<User>) userService.getAllByTeam(team);
        int counter = 0;
        for (User user: users
                ) {
            for (UserSkill userSkill: user.getUserSkills()
                    ) {
                if(userSkill.getSkill().equals(skill))
                {
                    counter++;
                }
            }
        }
        return counter;
    }



    @Test
    public void getSkillCountInTeam() {
        Mockito.when(userService.getAllByTeam(any())).thenReturn();
    }

    @Test
    public void getNonColleagueTeamOverview() throws Exception {
        User userFromAnotherDepartment = users.get(0);
        userFromAnotherDepartment.setTeam(teams.get(1));


        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);
        Mockito.when(userService.getUserById(userFromAnotherDepartment.getId())).thenReturn(userFromAnotherDepartment);

        TeamOverviewResponse resultResponse = teamService.getTeamOverview(testTeam.getId(), userFromAnotherDepartment.getId());

        Assert.assertThat(resultResponse, instanceOf(NonColleagueTeamOverviewResponse.class));

    }



}