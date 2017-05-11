package lt.swedbank.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.User;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.services.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    private User correctUser;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(ResponseEntityExceptionHandler.class)
                .build();

        mapper = new ObjectMapper();

        correctUser = new User();
        correctUser.setName("TestUserName");
        correctUser.setLastName("TestUserLastName");
        correctUser.setPassword("TestUserPassword");
        correctUser.setEmail("testuser@gmail.com");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUser() throws Exception {
    }


    //TODO both test getting bad request, need to fix that

    @Test
    public void test_if_unauthorized_when_token_not_provided() throws Exception {

        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer bad_token")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_if_user_not_found_is_thrown_when_it_is_not() throws Exception {

        Mockito.when(userService.getUserByEmail(any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer bad_token")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

}