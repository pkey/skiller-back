package lt.swedbank.services.team;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamEntityResponse;
import lt.swedbank.repositories.TeamRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;


public class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    private List<Team> teams;

    private List<TeamEntityResponse> teamEntityResponseList;

    private Team testTeam;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testTeam = new Team();
        testTeam.setId(Long.valueOf(0));
        testTeam.setName("Team Test");

        teams = new ArrayList<>();
        teams.add(testTeam);
        teams.add(testTeam);

        teamEntityResponseList = new ArrayList<>();
        teamEntityResponseList.add(new TeamEntityResponse(testTeam));
        teamEntityResponseList.add(new TeamEntityResponse(testTeam));
    }

    @Test
    public void getAllTeams() throws Exception {
        Mockito.when(teamRepository.findAll()).thenReturn(teams);

        List<Team> resultTeams = new ArrayList<>();
        resultTeams = (ArrayList<Team>) teamService.getAllTeams();

        Assert.assertEquals(teams, resultTeams);
    }

    @Test
    public void getTeamEntityResponseList() throws Exception {
        Mockito.when(teamRepository.findAll()).thenReturn(teams);

        List<TeamEntityResponse>resultTeams = (List<TeamEntityResponse>) teamService.getTeamEntityResponseList();

        Assert.assertEquals(teamEntityResponseList.size(), resultTeams.size());
    }

    @Test
    public void getTeamById() throws Exception {
        Mockito.when(teamRepository.findOne(testTeam.getId())).thenReturn(testTeam);

        Team resultTeam = teamService.getTeamById(testTeam.getId());

        Assert.assertEquals(resultTeam, testTeam);
    }

}