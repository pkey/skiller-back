package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.response.userSkill.ColleagueUserSkillResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.userSkill.UserSkillNotFoundException;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.UserSkillRepository;
import lt.swedbank.services.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.any;

public class UserSkillServiceTest {

    @InjectMocks
    private UserSkillService userSkillService;

    @Mock
    private SkillService skillService;

    @Mock
    private UserSkillRepository userSkillRepository;
    @Mock
    private UserSkillLevelService userSkillLevelService;
    @Mock
    private UserService userService;

    private AddSkillRequest addSkillRequest;
    private AssignSkillLevelRequest assignSkillLevelRequest;
    private Skill skill;
    private List<SkillLevel> skillLevels;
    private User user;
    private List<User> users;
    private UserSkill testUserSkill;
    private UserSkillLevel defaultUserSkillLevel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        skill = TestHelper.skills.get(0);
        skillLevels = TestHelper.skillLevels;

        addSkillRequest = new AddSkillRequest();
        addSkillRequest.setTitle(skill.getTitle());

        assignSkillLevelRequest = new AssignSkillLevelRequest(2L, skill.getId(), "Motivation");

        users = TestHelper.fetchUsers(3);
        user = users.get(0);

        testUserSkill = new UserSkill(user, skill);
        defaultUserSkillLevel = new UserSkillLevel(testUserSkill, TestHelper.defaultSkillLevel);
        testUserSkill.addUserSkillLevel(defaultUserSkillLevel);

    }

    @Test
    public void canAddUserSkill() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(null);
        Mockito.when(userService.getUserById(user.getId())).thenReturn(user);
        Mockito.when(userSkillLevelService.addDefaultUserSkillLevel(any(UserSkill.class))).thenReturn(defaultUserSkillLevel);
        Mockito.when(userSkillRepository.save(any(UserSkill.class))).thenReturn(testUserSkill);

        ColleagueUserSkillResponse colleagueUserSkillResponse = userSkillService.addUserSkill(user.getId(), addSkillRequest);

        Assert.assertEquals(testUserSkill.getSkill().getId(), colleagueUserSkillResponse.getId());
        Assert.assertEquals(testUserSkill.getSkill().getTitle(), colleagueUserSkillResponse.getTitle());
        Assert.assertEquals(testUserSkill.getUserSkillLevels().get(0).getSkillLevel().getId(), colleagueUserSkillResponse.getLevel().getId());
        Assert.assertEquals(testUserSkill.getUserSkillLevels().get(0).getVotes().size(), colleagueUserSkillResponse.getVotes().size());

        Mockito.verify(userSkillRepository, Mockito.times(1)).save(any(UserSkill.class));
        Mockito.verify(skillService, Mockito.times(0)).addSkill(any());

    }

    @Test(expected = SkillAlreadyExistsException.class)
    public void add_user_skill_fails_if_already_exists() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(testUserSkill);

        userSkillService.addUserSkill(user.getId(), addSkillRequest);
    }

    @Test
    public void add_user_skill_adds_new_skill_if_it_does_not_exist() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenThrow(new SkillNotFoundException());
        Mockito.when(skillService.addSkill(addSkillRequest)).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(null);
        Mockito.when(userService.getUserById(user.getId())).thenReturn(user);
        Mockito.when(userSkillLevelService.addDefaultUserSkillLevel(any(UserSkill.class))).thenReturn(defaultUserSkillLevel);
        Mockito.when(userSkillRepository.save(any(UserSkill.class))).thenReturn(testUserSkill);

        UserSkillResponse userSkillResponse = userSkillService.addUserSkill(user.getId(), addSkillRequest);

        Assert.assertEquals(testUserSkill.getSkill().getId(), userSkillResponse.getId());

        Mockito.verify(userSkillRepository, Mockito.times(1)).save(any(UserSkill.class));
        Mockito.verify(skillService, Mockito.times(1)).addSkill(any());

    }


    @Test(expected = SkillAlreadyExistsException.class)
    public void if_skill_already_exist_exception_is_thrown() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(testUserSkill);

        userSkillService.addUserSkill(user.getId(), addSkillRequest);
    }

    @Test
    public void removeUserSkill() throws Exception {
        Mockito.when(skillService.findById(skill.getId())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(testUserSkill);

        UserSkillResponse resultUserSkill = userSkillService.removeUserSkill(user.getId(), skill.getId());

        Assert.assertEquals(testUserSkill.getSkill().getId(), resultUserSkill.getId());

        Mockito.verify(userSkillRepository, Mockito.times(1)).delete(any(UserSkill.class));
    }

    @Test(expected = SkillNotFoundException.class)
    public void removing_not_existing_user_skill_throws_exception() throws Exception {
        Mockito.when(skillService.findById(skill.getId())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(null);

        UserSkillResponse resultUserSkill = userSkillService.removeUserSkill(user.getId(), skill.getId());
    }

    @Test
    public void assignSkillLevel() throws Exception {
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(
                user.getId(), assignSkillLevelRequest.getSkillId()))
                .thenReturn(testUserSkill);

        UserSkillLevel testUserSkillLevel = new UserSkillLevel(testUserSkill,
                skillLevels.get(1));

        testUserSkillLevel.setMotivation(assignSkillLevelRequest.getMotivation());

        Mockito.when(userSkillLevelService.addUserSkillLevel(
                testUserSkill, assignSkillLevelRequest))
                .thenReturn(testUserSkillLevel);

        UserSkill resultUserSkill = userSkillService.assignSkillLevel(user, assignSkillLevelRequest);

        Assert.assertEquals(assignSkillLevelRequest.getMotivation(), resultUserSkill.getUserSkillLevels().get(1).getMotivation());

        Mockito.verify(userSkillRepository, Mockito.times(1)).findByUserIdAndSkillId(any(), any());
    }

    @Test
    public void getUserSkillByUserIdAndSkillId(){
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(testUserSkill);

        UserSkill resultUserSkill = userSkillService.getUserSkillByUserIdAndSkillId(user.getId(), skill.getId());

        Assert.assertEquals(testUserSkill, resultUserSkill);
    }

    @Test(expected = UserSkillNotFoundException.class)
    public void throws_exception_if_no_user_skill_exists() throws Exception {
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(null);

        userSkillService.getUserSkillByUserIdAndSkillId(user.getId(), skill.getId());
    }

}