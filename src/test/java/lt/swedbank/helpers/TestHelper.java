package lt.swedbank.helpers;


import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.text.TextProducer;
import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<User> fetchUsers(int amount) {

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
            user.setTeam(fetchTeams(1).get(0));
            userList.add(user);
        }

        return userList;
    }

    public static List<Team> fetchTeams(int amount) {
        Fairy fairy = Fairy.create();

        List<Department> departments = fetchDepartment(amount);
        List<Team> teams = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            TextProducer textProducer = fairy.textProducer();

            Team team = new Team();
            team.setId(Integer.toUnsignedLong(i));
            team.setName("Team" + textProducer.word());
            team.setDepartment(departments.get(i));

            teams.add(team);
        }

        return teams;

    }

    public static List<Department> fetchDepartment(int amount) {
        Fairy fairy = Fairy.create();


        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            TextProducer textProducer = fairy.textProducer();

            Department department = new Department();
            department.setId(Integer.toUnsignedLong(i));
            department.setName("Team" + textProducer.word());
            departments.add(department);
        }

        return departments;

    }

}
