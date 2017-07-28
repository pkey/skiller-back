package lt.swedbank.controllers.team;


import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.team.TeamService;
import lt.swedbank.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;


    //Mock initialisation
    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;

    //Test Data
    private List<User> testUsers;
    private User testUser;
    private TeamWithUsersResponse testTeamOverviewResponse;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(teamController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        testUsers = TestHelper.fetchUsers(5);
        testUser = testUsers.get(0);
    }

    @Test
    public void getMyTeam() throws Exception {
        testTeamOverviewResponse = new ColleagueTeamOverviewWithUsersResponse(testUser.getTeam(), new ArrayList<>());

        Mockito.when(userService.getUserByAuthId(any())).thenReturn(testUser);
        Mockito.when(teamService.getMyTeam(testUser.getId())).thenReturn(testTeamOverviewResponse);


        mockMvc.perform(get("/team/my")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testTeamOverviewResponse.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testTeamOverviewResponse.getName())))
                .andExpect(jsonPath("$.division").exists())
                .andExpect(jsonPath("$.department").exists())
                .andExpect(jsonPath("$.users").isArray());

    }

    @Test
    public void getTeamOverviewOfColleaguesTeam() throws Exception {
        testTeamOverviewResponse = new ColleagueTeamOverviewWithUsersResponse(testUser.getTeam(), new ArrayList<>());

        Mockito.when(userService.getUserByAuthId(any())).thenReturn(testUser);
        Mockito.when(teamService.getTeamOverview(testUser.getTeam().getId(), testUser.getId())).thenReturn(testTeamOverviewResponse);


        mockMvc.perform(get("/team/0")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id", is(testTeamOverviewResponse.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testTeamOverviewResponse.getName())))
                .andExpect(jsonPath("$.division").exists())
                .andExpect(jsonPath("$.department").exists())
                .andExpect(jsonPath("$.users").isArray());

    }

}
