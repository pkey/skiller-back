package lt.swedbank.helpers;


import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import lt.swedbank.beans.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<User> fetchUsers(int amount){

        Fairy fairy = Fairy.create();

        List<User> userList = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            Person person = fairy.person();

            User user = new User();
            user.setId(Integer.toUnsignedLong(i));
            user.setName(person.getFirstName());
            user.setLastName(person.getLastName());
            user.setPassword("password");
            user.setEmail(person.getEmail());
            user.setUserSkills(new ArrayList<>());

            userList.add(user);
        }

        return userList;
    }

}
