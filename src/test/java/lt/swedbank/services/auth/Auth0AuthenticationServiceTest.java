package lt.swedbank.services.auth;

import com.auth0.client.auth.AuthAPI;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.AuthRequest;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.LoginUserRequest;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({AuthAPI.class, Auth0AuthenticationService.class})
public class Auth0AuthenticationServiceTest {

    @InjectMocks
    private Auth0AuthenticationService auth0AuthenticationService;

    @Mock
    private Unirest unirest;

    @Mock
    private UserService userService;


    @Mock
    private AuthAPI authAPI;


    private RegisterUserRequest registerUserRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        AuthAPI authAPIMock = Mockito.mock(AuthAPI.class);

        whenNew(AuthAPI.class).withAnyArguments().thenReturn(authAPIMock);

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

//
//        AuthAPI authAPIMock = Mockito.mock(AuthAPI.class);
//
//        HttpResponse response = Mockito.mock(HttpResponse.class);
//
//        mockStatic(Unirest.class);
//
//        when(Unirest.post(Mockito.anyString())).thenReturn());
//        RegisterUserResponse registerUserResponse = auth0AuthenticationService.registerUser(registerUserRequest);
//
//
//
//        Assert.assertEquals(registerUserResponse.getName(), registerUserResponse.getName());
    }

    @Test
    public void loginUser() throws Exception {
        AuthRequest request = Mockito.mock(AuthRequest.class);
        request.setAudience("something");

        TokenHolder tokenHolder = Mockito.mock(TokenHolder.class);

        when(authAPI.login(Mockito.anyString(), anyString())).thenReturn(request);
        when(request.execute()).thenReturn(tokenHolder);

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setConnection("connection");
        loginUserRequest.setEmail("connection");
        loginUserRequest.setPassword("connection");

        auth0AuthenticationService.loginUser(loginUserRequest);


    }

    @Test
    public void extractAuthIdFromToken() throws Exception {
    }

}