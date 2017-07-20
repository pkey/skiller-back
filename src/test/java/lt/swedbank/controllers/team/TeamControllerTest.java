package lt.swedbank.controllers.team;


import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.team.TeamService;
import lt.swedbank.services.user.UserService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

public class TeamControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(teamController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();


    }
}
