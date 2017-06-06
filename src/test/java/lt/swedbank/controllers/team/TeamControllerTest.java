package lt.swedbank.controllers.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamEntityResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.services.team.TeamService;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TeamControllerTest {

    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private List<Team> teams;
    private List<TeamEntityResponse> teamEntityResponseList;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        mockMvc = MockMvcBuilders
                .standaloneSetup(teamController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        Team tempTeam1 = new Team();
        tempTeam1.setId(Long.parseLong("1"));
        tempTeam1.setName("Team1");

        Team tempTeam2 = new Team();
        tempTeam2.setName("Team2");
        tempTeam2.setId(Long.parseLong("2"));

        teams = new ArrayList<>();
        teams.add(tempTeam1);
        teams.add(tempTeam2);

        teamEntityResponseList = new ArrayList<>();
        teamEntityResponseList.add(new TeamEntityResponse(tempTeam1));
        teamEntityResponseList.add(new TeamEntityResponse(tempTeam2));

    }

    @Test
    public void get_all_user_success() throws Exception {
        Mockito.when(teamService.getTeamEntityResponseList()).thenReturn(teamEntityResponseList);

        mockMvc.perform(get("/teams/all")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].name", is("Team1")))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].name", is("Team2")))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(teamService, times(1)).getTeamEntityResponseList();
        verifyNoMoreInteractions(teamService);
    }


}

