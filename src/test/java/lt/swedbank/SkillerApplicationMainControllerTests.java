package lt.swedbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.User;
import lt.swedbank.controllers.MainController;
import lt.swedbank.services.Auth0AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        user.setUsername("TestUser");
        user.setPassword("TestUserPassword");
        user.setEmail("testuser@gmail.com");
        user.setConnection("Username-Password-Authentication");
    }

    @Test
    public void loginTest() throws Exception {


        Mockito.when(auth0AuthenticationService.registerUser(any())).thenReturn(user);

        String bookmarkJson = mapper.writeValueAsString(user);

        //String expectedJson = mapper.writeValueAsString();

        this.mockMvc.perform(post("/login")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk());
                //.andExpect(content().json(expectedJson));
    }

    @Test
    public void getTest()
    {

        //mockMvc.perform(get("/get");

        //Mockito.when(auth0AuthenticationService.getUser(any())).thenReturn(this.user);

    }



}
    
