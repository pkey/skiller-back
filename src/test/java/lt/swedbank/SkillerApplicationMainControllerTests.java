package lt.swedbank;

import com.auth0.exception.Auth0Exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.User;
import lt.swedbank.controllers.MainController;
import lt.swedbank.services.Auth0AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SkillerApplicationMainControllerTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8"));

    private MockMvc mockMvc;

    private User user;

    @InjectMocks
    private MainController mainController;

    @Mock
    private Auth0AuthenticationService auth0AuthenticationService;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController)./*addFilters(new CorsFilter()).*/build();

        this.mapper = new ObjectMapper();


        user = new User();
    }

    @Test
    public void loginTest() throws Exception {
        user.setUsername("TestUser");
        user.setPassword("TestUserPassword");
        user.setEmail("testuser@gmail.com");
        user.setConnection("Username-Password-Authentication");

        Mockito.when(auth0AuthenticationService.registerUser(any())).thenReturn(user);

        String bookmarkJson = mapper.writeValueAsString(user);

        //String expectedJson = mapper.writeValueAsString();

        this.mockMvc.perform(post("/login")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk());
                //.andExpect(content().json(expectedJson));
    }



}
