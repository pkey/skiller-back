package lt.swedbank.helpers;


import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import lt.swedbank.beans.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<User> fetchUsers(int amount){

        Fairy fairy = Fairy.create();

        String[] names = {"John", "Jane", "Jane", "Sam"};
        String[] lastNames = {"Lifter", "Uber", "Vuber", "Smith"};


        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Person person = fairy.person();

            User user = new User();
            user.setId(Integer.toUnsignedLong(i));
            user.setName(names[i]);
            user.setLastName(lastNames[i]);
            user.setPassword("password");
            user.setEmail(person.getEmail());
            user.setUserSkills(new ArrayList<>());

            userList.add(user);
        }

        return userList;
    }

}
