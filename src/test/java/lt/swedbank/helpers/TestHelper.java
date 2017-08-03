package lt.swedbank.helpers;


import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import io.codearte.jfairy.producer.text.TextProducer;
import lt.swedbank.beans.entity.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
Class to generate test data
 */
public class TestHelper {

    public static final int NUMBER_OF_DIVISIONS = 2;
    public static final int NUMBER_OF_DEPARTMENTS = 5;
    public static final int NUMBER_OF_TEAMS = 10;
    public static final int NUMBER_OF_USERS = 50;
    public static final int NUMBER_OF_SKILLS = 10;
    public static final int NUMBER_OF_SKILLS_USER_HAS = 5;
    public static final int NUMBER_OF_SKILL_LEVELS = 3;
    public static final int NUMBER_OF_VALUE_STREAM = 3;


    public static int currentUserSkillId = 0;
    public static int currentSkillLevelId = 0;
    public static List<Skill> skills;
    public static List<SkillLevel> skillLevels; //Change to enumeration
    private static List<User> userList;
    private static List<Team> teams;
    private static List<Department> departments;
    private static List<Division> divisions;
    public static SkillLevel defaultSkillLevel;
    private static List<ValueStream> valueStreams;



    static {
        createDivisions();
        createDepartments();
        createTeams();
        createLevels();
        createSkills();
        createUsers();
        createValueStreams();
    }

    private TestHelper(){
        throw new UnsupportedOperationException();
    }

    private static void createLevels() {

        String[] levelNames = {"Novice", "Amateur", "Pro"};
        String[] levelDescriptions = {"Novice Desc", "Amateur Desc", "Pro Desc"};

        skillLevels = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_SKILL_LEVELS; i++) {
            SkillLevel skillLevel = new SkillLevel((long) (i + 1),levelNames[i], levelDescriptions[i]);
            skillLevel.setId(Integer.toUnsignedLong(i));
            skillLevel.setLevel(Integer.toUnsignedLong(i) + 1);

            skillLevels.add(skillLevel);
        }

        defaultSkillLevel = skillLevels.get(0);

    }

    private static void createUsers(){
        Fairy fairy = Fairy.create();

        userList = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            Person person = fairy.person();

            User user = new User();
            user.setId(Integer.toUnsignedLong(i));
            user.setName(person.getFirstName());
            user.setLastName(person.getLastName());
            user.setPassword("password");
            user.setEmail(person.getEmail());

            user.setUserSkills(createUserSkills());

            user.setConnection("connection");

            Team team = teams.get(i % NUMBER_OF_TEAMS);
            team.addUser(user);
            user.setTeam(team);

            userList.add(user);
        }
    }

    private static void createSkills(){
        Fairy fairy = Fairy.create();

        String[] skillNames = {"Angular", "Java", "Testing", "TDE", "Javascript", "Typescript", "Football", "Basketball", "Mochito", "Dancing"};

        skills = new ArrayList<>();

        for (int i = 0; i < skillNames.length; i++) {
            TextProducer textProducer = fairy.textProducer();

            Skill skill = new Skill();
            skill.setId(Integer.toUnsignedLong(i));
            skill.setTitle(textProducer.word());

            skills.add(skill);
        }
    }

    private static List<UserSkill> createUserSkills() {

        List<UserSkill> userSkills = new ArrayList<>();

        Set<Integer> alreadyAddedSkills = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_SKILLS_USER_HAS; i++) {

            int randomSkillId = ThreadLocalRandom.current().nextInt(NUMBER_OF_SKILLS - 1);


            if(!alreadyAddedSkills.contains(randomSkillId)) {
                UserSkill userSkill = new UserSkill();
                userSkill.setId(Integer.toUnsignedLong(currentUserSkillId++));
                userSkill.setSkill(skills.get(randomSkillId));


                userSkill.addUserSkillLevel(createUserSkillLevel(userSkill, defaultSkillLevel));
                userSkills.add(userSkill);
            } else {
                i--;
            }

            alreadyAddedSkills.add(randomSkillId);

        }

        return userSkills;
    }

    public static UserSkillLevel createUserSkillLevel(UserSkill userSkill, SkillLevel skillLevel) {
        UserSkillLevel userSkillLevel = new UserSkillLevel();
        userSkillLevel.setId(Integer.toUnsignedLong(currentSkillLevelId++));
        userSkillLevel.setUserSkill(userSkill);
        userSkillLevel.setSkillLevel(skillLevel);
        userSkillLevel.setValidFrom(new Date());
        userSkillLevel.setVotes(new ArrayList<>());
        userSkillLevel.setApproved();

        return userSkillLevel;
    }

    private static void createTeams(){
        Fairy fairy = Fairy.create();

        teams = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_TEAMS; i++) {
            TextProducer textProducer = fairy.textProducer();
            Department department = departments.get(i % NUMBER_OF_DEPARTMENTS);

            Team team = new Team("Team " + textProducer.word(), department);
            team.setId(Integer.toUnsignedLong(i));
            team.setName("Team" + textProducer.word());

            department.addTeam(team);

            teams.add(team);
        }

    }


    private static void createDepartments(){
        Fairy fairy = Fairy.create();

        departments = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_DEPARTMENTS; i++) {
            TextProducer textProducer = fairy.textProducer();

            Department department = new Department();
            department.setId(Integer.toUnsignedLong(i));
            department.setName("Department" + textProducer.word());
            department.setDivision(divisions.get(i%NUMBER_OF_DIVISIONS));

            departments.add(department);
        }
    }

    private static void createDivisions(){
        Fairy fairy = Fairy.create();

        divisions = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_DIVISIONS; i++) {
            TextProducer textProducer = fairy.textProducer();

            Division division = new Division();
            division.setId(Integer.toUnsignedLong(i));
            division.setName("Division" + textProducer.word());

            divisions.add(division);
        }
    }

    private static void createValueStreams() {
        List<ValueStream> valueStreamsToBeGenerated = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_VALUE_STREAM; i++) {
            ValueStream valueStream = generateValueStream(i);
            valueStreamsToBeGenerated.add(valueStream);
        }

        valueStreams = valueStreamsToBeGenerated;
    }

    private static ValueStream generateValueStream(int i) {
        ValueStream valueStream = new ValueStream();
        valueStream.setId((long) i);
        valueStream.setName(i + " - Value Stream");

        return valueStream;
    }

    public static List<User> fetchUsers(int amount) {
        return userList.subList(0, amount);
    }


    public static List<Team> fetchTeams(int amount) {
        return teams.subList(0, amount);
    }

    public static List<Department> fetchDepartments(int amount) {
        return departments.subList(0, amount);
    }

    public static List<Division> fetchDivisions(int amount) {
        return divisions.subList(0, amount);
    }



    public static List<ValueStream> fetchValueStreams() {
        return valueStreams;
    }

    public static List<Skill> fetchSkills(int ammount) {
        return skills.subList(0, ammount);
    }

}
