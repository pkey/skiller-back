package lt.swedbank.services.overview;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.helpers.TestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class OverviewServiceTest {

    @InjectMocks
    private OverviewService overviewService;


    private Team testTeam;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testTeam = TestHelper.fetchTeams(1).get(0);
    }

    @Test
    public void getUserAverageSkillLevel() throws Exception {
        List<User> testUsers = TestHelper.fetchUsers(2);
        for (User testUser : testUsers) {
            testUser.setTeam(testTeam);
        }

        Skill testSkill = new Skill("Test Skill");

        //Change user skill levels from default to test better
        for (User user : testUsers) {

            UserSkill testUserSkill = new UserSkill(user, testSkill);
            testUserSkill.addUserSkillLevel(TestHelper.createUserSkillLevel(testUserSkill, TestHelper.skillLevels.get(1)));

            user.addUserSkill(testUserSkill);
        }

        Team testTeam = TestHelper.fetchTeams(1).get(0);
        testTeam.setUsers(testUsers);

        Assert.assertEquals(2L, overviewService.getUserAverageSkillLevel(testTeam.getUsers(), testSkill), 0.0002);
    }

    @Test
    public void getUserSkillCount() throws Exception {
        Skill testSkill = testTeam.getUsers().get(0).getUserSkills().get(0).getSkill();

        Assert.assertEquals((overviewService.getUserSkillCount(testTeam.getUsers(), testSkill) > 0), true);
    }

}