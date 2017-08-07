package lt.swedbank.user;

import lt.swedbank.beans.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserBeanTest {

    private User user1;
    private User user2;

    @Before
    public void setUp(){
        user1 = new User();
        user1.setId(1L);
        user2 = new User();
        user2.setId(1L);
    }

    @Test
    public void equals(){
        Assert.assertEquals(true, user1.equals(user2));
    }

    @Test
    public void equalsFalse() {
        Assert.assertEquals(false, user1.equals(null));
    }

}
