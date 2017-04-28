package lt.swedbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.User;
import lt.swedbank.controllers.MainController;
import lt.swedbank.services.Auth0AuthenticationService;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.constraints.Null;
import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SkillerApplicationMainControllerTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8"));

    private MockMvc mockMvc;

    private User correctUser;

    @InjectMocks
    private MainController mainController;

    @Mock
    private Auth0AuthenticationService auth0AuthenticationService;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(mainController)./*addFilters(new CorsFilter()).*/build();

        mapper = new ObjectMapper();

        correctUser = new User();
        correctUser.setName("TestUserName");
        correctUser.setLastName("TestUserLastName");
        correctUser.setPassword("TestUserPassword");
        correctUser.setEmail("testuser@gmail.com");
        correctUser.setConnection("Username-Password-Authentication");
    }

    @Test
    public void loginTest_goodUserJson() throws Exception {

        String bookmarkJson = mapper.writeValueAsString(correctUser);

        mockMvc.perform(post("/login")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk());
                //.andDo(MockMvcResultHandlers.print());
                //.andExpect(jsonPath("$..access_token").value(IsNotNull()));
    }

    @Test
    public void registerTest_goodUserJson() throws Exception {

        String bookmarkJson = mapper.writeValueAsString(correctUser);

        mockMvc.perform(post("/register")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk());
    }
/*  "name": "name",
  "lastName": "Lastname",
  "email": "saulute3200@gmail.com"*/

    @Test
    public void getTest_plain() throws Exception {

        mockMvc.perform(get("/get")
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTest_Unauthorized() throws Exception {

        String bookmarkJson = mapper.writeValueAsString(correctUser);

        mockMvc.perform(get("/get")//http://localhost:8080
                .contentType(contentType)
                .content(bookmarkJson)
                .header("Content-Type", "application/json"))
                .andExpect(status().isBadRequest());
    }


}
    
