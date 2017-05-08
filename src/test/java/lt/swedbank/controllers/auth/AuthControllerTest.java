package lt.swedbank.controllers.auth;

import com.auth0.exception.Auth0Exception;
import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.User;
import lt.swedbank.beans.request.LoginUserRequest;
import lt.swedbank.beans.request.RegisterUserRequest;

import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.services.auth.Auth0AuthenticationService;


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

import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8"));

    private MockMvc mockMvc;

    private User correctUser;


    @InjectMocks
    private AuthController authController;

    @Mock
    private Auth0AuthenticationService auth0AuthenticationService;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        correctUser = new User();
        correctUser.setName("TestUserName");
        correctUser.setLastName("TestUserLastName");
        correctUser.setPassword("TestUserPassword");
        correctUser.setEmail("testuser@gmail.com");
        correctUser.setConnection("Username-Password-Authentication");
    }

    @Test
    public void login_good_user_json() throws Exception {

        String bookmarkJson = mapper.writeValueAsString(new LoginUserRequest(correctUser));

        mockMvc.perform(post("/login")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk());

        verify(auth0AuthenticationService, times(1)).loginUser(any());
        verifyNoMoreInteractions(auth0AuthenticationService);
    }

    @Test
    public void register_user_good_json() throws Exception {

        String bookmarkJson = mapper.writeValueAsString(new RegisterUserRequest(correctUser));

        when(auth0AuthenticationService.registerUser(any())).thenReturn(correctUser);
        mockMvc.perform(post("/register")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")));
        verify(auth0AuthenticationService, times(1)).registerUser(any());
        verifyNoMoreInteractions(auth0AuthenticationService);
    }

    @Test
    public void returns_bad_request_with_message_if_email_or_password_is_wrong() throws Exception {
        Mockito.when(auth0AuthenticationService.loginUser(any())).thenThrow(new Auth0Exception("Message"));

        String bookmarkJson = mapper.writeValueAsString(correctUser);

        Mockito.verify(this.auth0AuthenticationService, Mockito.times(0)).loginUser(any());

        mockMvc.perform(post("/login")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void get_user_success() throws Exception {

        when(auth0AuthenticationService.getUser(any())).thenReturn(correctUser);
        mockMvc.perform(get("/get").header("Authorization", "Bearer"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))//Irrelevant while skills array is hardcoded
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")));
        verify(auth0AuthenticationService, times(1)).getUser(any());
        verifyNoMoreInteractions(auth0AuthenticationService);
    }

    @Test
    public void get_user_auth0_exception() throws Exception {

        when(auth0AuthenticationService.getUser(any())).thenThrow(new Auth0Exception("mocked Auth0 exception"));
        mockMvc.perform(get("/get").header("Authorization", ""))
                .andExpect(status().isInternalServerError());
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));//Irrelevant while skills array is hardcoded
        verify(auth0AuthenticationService, times(1)).getUser(any());
        verifyNoMoreInteractions(auth0AuthenticationService);
    }

}
    
