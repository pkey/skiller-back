package lt.swedbank.repositories.search;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.repositories.SkillRepository;
import lt.swedbank.repositories.UserRepository;
import lt.swedbank.repositories.UserSkillRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class UserSearchTest {

    @Autowired
    private UserSearch userSearch;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private List<User> testUserList;


    @Before
    public void setUp() throws Exception {

        testUserList = generateUserList();

        userRepository.save(testUserList);

        FullTextEntityManager ftem = Search.getFullTextEntityManager(this.entityManager);

        this.entityManager.flush();

        ftem.flushToIndexes();
    }


    private List<User> generateUserList() {
        List<User> userList = new ArrayList<>();

        for(int i = 1; i < 5; i++){
            userList.add(generateUser(i));
        }

        return userList;
    }

    private User generateUser(int id){
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setName("Name " + id);
        user.setLastName("Last Name " + id);
        user.setPassword("password " + id);
        user.setEmail("testuser" + id + "@gmail.com");

        return user;
    }

    @Test
    public void searchUsersByName() throws Exception {

        Set<User> resultUserList = userSearch.search("Name");

        Assert.assertEquals(testUserList.size(), resultUserList.size());
    }

}