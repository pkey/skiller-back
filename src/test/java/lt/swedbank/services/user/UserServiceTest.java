package lt.swedbank.services.user;

import lt.swedbank.beans.entity.User;
import lt.swedbank.exceptions.ApplicationException;
import lt.swedbank.handlers.ExceptionHandler;
import lt.swedbank.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    private User testUser;


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExceptionHandler exceptionHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.testUser = new User();
        testUser.setId(new Long(0));
        testUser.setName("Testas");
        testUser.setLastName("Testauskas");
        testUser.setEmail("test@test.com");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getUserByEmail() throws Exception {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(testUser);
        User resultUser = userService.getUserByEmail("something");
        assertEquals(testUser.getEmail(), resultUser.getEmail());
    }

    @Test(expected = ApplicationException.class)
    public void throws_use_does_not_exist_error() throws Exception{
        Mockito.when(userRepository.findByEmail(any())).thenReturn(null);
        Mockito.when(exceptionHandler.handleException(any())).thenReturn(new ApplicationException("error"));
        User resultUser = userService.getUserByEmail("something");
    }

}