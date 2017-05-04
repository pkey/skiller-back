package lt.swedbank.services.user;

import lt.swedbank.beans.User;
import lt.swedbank.controllers.user.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by paulius on 5/4/17.
 */
public class UserServiceTest {

    private User testUser;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception {
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

    }

}