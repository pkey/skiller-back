package lt.swedbank.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.User;
import lt.swedbank.services.user.UserService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by paulius on 5/4/17.
 */
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

        mockMvc = MockMvcBuilders.standaloneSetup(userController)./*addFilters(new CorsFilter()).*/build();

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


    @Test
    public void test_if_unauthorized_when_token_not_provided() throws Exception {

        //TODO Write a test for unauthorized access. Need to provide good headers.
        mockMvc.perform(get("/user/get")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }



}