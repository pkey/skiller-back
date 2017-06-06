package lt.swedbank.services.auth;

import com.auth0.client.auth.AuthAPI;
import com.mashape.unirest.http.Unirest;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.RegisterUserRequest;
import lt.swedbank.beans.response.RegisterUserResponse;
import lt.swedbank.services.user.UserService;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AUTH;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({AuthAPI.class, Auth0AuthenticationService.class})
public class Auth0AuthenticationServiceTest {

    @InjectMocks
    private Auth0AuthenticationService auth0AuthenticationService;

    @Mock
    private Unirest unirest;

    @Mock
    private UserService userService;

//    @Mock
//    private AuthAPI authAPI;


    private RegisterUserRequest registerUserRequest;

    @Before
    public void setUp() throws Exception {
        AuthAPI authAPIMock = Mockito.mock(AuthAPI.class);

        whenNew(AuthAPI.class).withAnyArguments().thenReturn(authAPIMock);

        auth0AuthenticationService = new Auth0AuthenticationService("something", "something", "something");

        //MockitoAnnotations.initMocks(this);

        registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setName("Test");
        registerUserRequest.setLastName("Tester");
        registerUserRequest.setConnection("connection");
        registerUserRequest.setEmail("email");
        registerUserRequest.setPassword("password");



    }

    @Test
    public void registerUser() throws Exception {


        AuthAPI authAPIMock = Mockito.mock(AuthAPI.class);

        HttpResponse response = Mockito.mock(HttpResponse.class);

        whenNew(HttpResponse.class).withArguments("something").thenReturn(response);



        RegisterUserResponse registerUserResponse = auth0AuthenticationService.registerUser(registerUserRequest);



        Assert.assertEquals(registerUserResponse.getName(), registerUserResponse.getName());
    }

    @Test
    public void loginUser() throws Exception {
    }

    @Test
    public void extractAuthIdFromToken() throws Exception {
    }

}