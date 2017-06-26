package lt.swedbank.services.team;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.TeamOverviewResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;


public class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    private List<Team> teams;

    private Team testTeam;

    private List<User> users;

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