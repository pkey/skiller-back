package lt.swedbank.interceptors.auth;

import com.auth0.exception.Auth0Exception;
import lt.swedbank.beans.User;
import lt.swedbank.services.auth.AuthenticationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.HandlerExecutionChain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthInterceptorTest {


    @InjectMocks
    private AuthInterceptor authInterceptor;

    @Mock
    private AuthenticationService authenticationService;

    private User testUser;

    private Object handler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.testUser = new User();
        testUser.setId(new Long(0));
        testUser.setName("Testas");
        testUser.setLastName("Testauskas");
        testUser.setEmail("test@test.com");


        HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain(authInterceptor);

        this.handler = handlerExecutionChain.getHandler();



    }

    @After
    public void tearDown() throws Exception {
    }



    @Test
    public void test_if_pre_handle_adds_email_to_a_request() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/get");
        request.setMethod("GET");



        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(authenticationService.getUser(any())).thenReturn(this.testUser);

        authInterceptor.preHandle(request, response, this.handler);

        Mockito.verify(this.authenticationService, Mockito.times(1)).getUser(any());

        assertEquals(testUser.getEmail(), request.getAttribute("email"));
    }

    @Test(expected=Auth0Exception.class)
    public void test_if_error_is_thrown_when_service_fails() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/get");
        request.setMethod("GET");



        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(authenticationService.getUser(any())).thenThrow(new Auth0Exception("..."));

        Mockito.verify(this.authenticationService, Mockito.times(0)).getUser(any());



        authInterceptor.preHandle(request, response, this.handler);


    }



}