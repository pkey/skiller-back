package lt.swedbank.helpers;


import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
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

    public static List<RequestNotification> fetchRequestNotifications(int amount, ApprovalRequest approvalRequest){

        List<User> userList = fetchUsers(amount);
        List<RequestNotification> notificationList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            RequestNotification requestNotification = new RequestNotification(userList.get(0), approvalRequest);
            notificationList.add(requestNotification);
        }
        return notificationList;
    }

}
