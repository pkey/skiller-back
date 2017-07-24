package lt.swedbank.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import lt.swedbank.helpers.TestHelper;
import org.junit.After;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    private User testUser;
    private UserSkill newlyAddedUserSkill;
    private List<UserEntityResponse> testUserEntityResponseList;
    private UserEntityResponse testUserEntityResponse;
    private List<User> testUsers;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        testUsers = TestHelper.fetchUsers(5);


        testUser = testUsers.get(0);

        testUserEntityResponse = new UserEntityResponse(testUser);

        testUserEntityResponseList = new ArrayList<>();
        testUserEntityResponseList.add(testUserEntityResponse);

        newlyAddedUserSkill = new UserSkill(testUser, new Skill("SkillToAddLater"));

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void get_user_profile_success() throws Exception {
        UserEntityResponse userEntityResponseTest = mock(UserEntityResponse.class);

        when(userService.getUserProfile(anyLong(), anyString())).thenReturn(testUserEntityResponse);

        whenNew(UserEntityResponse.class).withAnyArguments().thenReturn(userEntityResponseTest);

        mockMvc.perform(get("/user/profile/0")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.lastName", is(testUser.getLastName())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.skills", hasSize(TestHelper.NUMBER_OF_SKILLS_USER_HAS)));

        verify(userService, times(1)).getUserProfile(any(), any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void get_user_success() throws Exception {


        when(userService.getUserByAuthId(any())).thenReturn(testUser);

        UserEntityResponse userEntityResponseTest = mock(UserEntityResponse.class);

        whenNew(UserEntityResponse.class).withAnyArguments().thenReturn(userEntityResponseTest);

        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.lastName", is(testUser.getLastName())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.skills", hasSize(TestHelper.NUMBER_OF_SKILLS_USER_HAS)));

        verify(userService, times(1)).getUserByAuthId(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void add_skill_to_user_success() throws Exception {

        String skillJson = mapper.writeValueAsString(new AddSkillRequest(newlyAddedUserSkill));

        when(userService.getUserByAuthId(any())).thenReturn(testUser);
        when(userService.addUserSkill(any(), any())).thenReturn(testUser);
        when(userService.getUserById(any())).thenReturn(testUser);

        UserEntityResponse userEntityResponseTest = mock(UserEntityResponse.class);

        whenNew(UserEntityResponse.class).withAnyArguments().thenReturn(userEntityResponseTest);

        mockMvc.perform(post("/user/skill/add").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.lastName", is(testUser.getLastName())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.skills", hasSize(TestHelper.NUMBER_OF_SKILLS_USER_HAS)));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).addUserSkill(any(), any());
        verify(userService, times(1)).getUserById(any());

    }

    @Test
    public void remove_skill_from_user_success() throws Exception {

        String skillJson = mapper.writeValueAsString(new RemoveSkillRequest(newlyAddedUserSkill));

        UserEntityResponse userEntityResponseTest = mock(UserEntityResponse.class);

        whenNew(UserEntityResponse.class).withAnyArguments().thenReturn(userEntityResponseTest);

        when(userService.getUserByAuthId(any())).thenReturn(testUser);
        when(userService.removeUserSkill(any(), any())).thenReturn(testUser);

        mockMvc.perform(post("/user/skill/remove").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.lastName", is(testUser.getLastName())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())))
                .andExpect(jsonPath("$.skills", hasSize(TestHelper.NUMBER_OF_SKILLS_USER_HAS)));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).removeUserSkill(any(), any());

    }
}