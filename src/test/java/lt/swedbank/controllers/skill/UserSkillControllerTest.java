package lt.swedbank.controllers.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.beans.response.userSkill.ColleagueUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserSkillControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private ObjectMapper mapper;
    private MockMvc mockMvc;

    @InjectMocks
    private UserSkillController userSkillController;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserSkillService userSkillService;

    private List<User> testUsers;
    private User testUser;
    private UserSkill newlyAddedUserSkill;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(userSkillController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        testUsers = TestHelper.fetchUsers(5);
        testUser = testUsers.get(0);

        newlyAddedUserSkill = new UserSkill(testUser, TestHelper.skills.get(0));


    }

    @Test
    public void addSkillToUserIsSuccessful() throws Exception {
        UserSkillLevel userSkillLevel = new UserSkillLevel(newlyAddedUserSkill, TestHelper.defaultSkillLevel);
        userSkillLevel.setVotes(new ArrayList<>());

        newlyAddedUserSkill.addUserSkillLevel(userSkillLevel);
        String skillJson = mapper.writeValueAsString(new AddSkillRequest(newlyAddedUserSkill));

        when(userService.getUserByAuthId(any())).thenReturn(testUser);
        when(userService.getUserById(any())).thenReturn(testUser);
        when(userSkillService.addUserSkill(any(), any())).thenReturn(new ColleagueUserSkillResponse(newlyAddedUserSkill.getSkill(), userSkillLevel));

        mockMvc.perform(post("/user/skill").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newlyAddedUserSkill.getSkill().getId().intValue())))
                .andExpect(jsonPath("$.title", is(newlyAddedUserSkill.getSkill().getTitle())))
                .andExpect(jsonPath("$.votes").isEmpty())
                .andExpect(jsonPath("$.level").exists());

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userSkillService, times(1)).addUserSkill(any(), any());

    }

    @Test
    public void add_lower_case_skill_title_will_fail() throws Exception {

        AddSkillRequest addSkillRequest = new AddSkillRequest();
        addSkillRequest.setTitle("lower");
        String skillJson = mapper.writeValueAsString(addSkillRequest);

        mockMvc.perform(post("/user/skill").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void add_empty_skill_title_will_fail() throws Exception {

        AddSkillRequest addSkillRequest = new AddSkillRequest();
        addSkillRequest.setTitle("");
        String skillJson = mapper.writeValueAsString(addSkillRequest);

        mockMvc.perform(post("/user/skill").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void add_null_skill_title_will_fail() throws Exception {

        AddSkillRequest addSkillRequest = new AddSkillRequest();
        addSkillRequest.setTitle(null);
        String skillJson = mapper.writeValueAsString(addSkillRequest);

        mockMvc.perform(post("/user/skill").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void remove_skill_from_user_success() throws Exception {
        UserWithSkillsResponse userEntityResponseTest = mock(UserWithSkillsResponse.class);
        whenNew(UserWithSkillsResponse.class).withAnyArguments().thenReturn(userEntityResponseTest);

        when(userService.getUserByAuthId(any())).thenReturn(testUser);
        when(userSkillService.removeUserSkill(any(), any())).thenReturn(new UserSkillResponse(newlyAddedUserSkill.getSkill()));

        mockMvc.perform(delete("/user/skill/" + newlyAddedUserSkill.getSkill().getId().toString()).header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newlyAddedUserSkill.getSkill().getId().intValue())))
                .andExpect(jsonPath("$.title", is(newlyAddedUserSkill.getSkill().getTitle())));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userSkillService, times(1)).removeUserSkill(any(), any());

    }
}
