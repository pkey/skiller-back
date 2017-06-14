package lt.swedbank.helpers;


import lt.swedbank.beans.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<User> fetchUsers(int amount){
        List<User> userList = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            User user = new User();
            user.setId(Integer.toUnsignedLong(i));
            user.setName("TestUserName");
            user.setLastName("TestUserLastName");
            user.setPassword("TestUserPassword");
            user.setEmail("testuser@gmail.com");
            user.setUserSkills(new ArrayList<>());

            userList.add(user);
        }

        return userList;
    }

}
