package lt.swedbank.controllers.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    private MockMvc mockMvc;

    private User testUser;
    private List<UserResponse> testUserEntityResponseList;
    private UserResponse testUserEntityResponse;
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

        testUsers = TestHelper.fetchUsers(5);

        testUser = testUsers.get(0);

        testUserEntityResponse = new UserResponse(testUser);

        testUserEntityResponseList = new ArrayList<>();
        testUserEntityResponseList.add(testUserEntityResponse);
        testUserEntityResponseList.add(new UserResponse(testUsers.get(1)));


    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void get_user_profile_success() throws Exception {
        when(userService.getUserProfile(anyLong(), anyString())).thenReturn(testUserEntityResponse);

        mockMvc.perform(get("/user/0")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUserEntityResponse.getName())))
                .andExpect(jsonPath("$.lastName", is(testUserEntityResponse.getLastName())))
                .andExpect(jsonPath("$.email", is(testUserEntityResponse.getEmail())));

        verify(userService, times(1)).getUserProfile(any(), any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void get_user_success() throws Exception {

        when(userService.getMyProfile(testUser.getAuthId())).thenReturn(testUserEntityResponse);

        mockMvc.perform(get("/user")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(testUser.getName())))
                .andExpect(jsonPath("$.lastName", is(testUser.getLastName())))
                .andExpect(jsonPath("$.email", is(testUser.getEmail())));

    }

    @Test
    public void allUsersAndTheirPropertiesAreReturned() throws Exception {
        when(userService.getAllUserResponses()).thenReturn(testUserEntityResponseList);

        mockMvc.perform(get("/user/all")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(testUsers.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(testUsers.get(0).getName())))
                .andExpect(jsonPath("$[0].lastName", is(testUsers.get(0).getLastName())))
                .andExpect(jsonPath("$[0].email", is(testUsers.get(0).getEmail())))
                .andExpect(jsonPath("$[0].team").exists())
                .andExpect(jsonPath("$[1].id", is(testUsers.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(testUsers.get(1).getName())))
                .andExpect(jsonPath("$[1].lastName", is(testUsers.get(1).getLastName())))
                .andExpect(jsonPath("$[1].email", is(testUsers.get(1).getEmail())))
                .andExpect(jsonPath("$[1].team").exists());

    }
}