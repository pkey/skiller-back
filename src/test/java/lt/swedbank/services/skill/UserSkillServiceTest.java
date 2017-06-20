package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.userSkill.UserSkillNotFoundException;
import lt.swedbank.repositories.UserSkillRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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

    private AddSkillRequest addSkillRequest;
    private RemoveSkillRequest removeSkillRequest;
    private AssignSkillLevelRequest assignSkillLevelRequest;

    private Skill skill;

    private User user;

    private UserSkill testUserSkill;

    private List<UserSkillLevel> testUserSkillLevels;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        skill = new Skill();
        skill.setTitle("Angular");
        skill.setId(Long.valueOf(0));

        addSkillRequest = new AddSkillRequest();
        addSkillRequest.setTitle("Angular");

        removeSkillRequest = new RemoveSkillRequest();
        removeSkillRequest.setTitle("Angular");

        assignSkillLevelRequest = new AssignSkillLevelRequest();
        assignSkillLevelRequest.setLevelId(Long.valueOf(2));
        assignSkillLevelRequest.setSkillId(skill.getId());
        assignSkillLevelRequest.setMotivation("Motivation");

        user = new User();
        user.setId(Long.valueOf(0));

        testUserSkill = new UserSkill(user, skill);

        UserSkillLevel testUserSkillLevel = new UserSkillLevel(testUserSkill,
                new SkillLevel("Level title", "Level Description"));

        UserSkillLevel testDefaultUserSkillLevel = new UserSkillLevel(testUserSkill,
                new SkillLevel("Default Level title", "Default Level Description"));

        testUserSkillLevels = new ArrayList<>();
        testUserSkillLevels.add(testDefaultUserSkillLevel);

        testUserSkill.setUserSkillLevels(testUserSkillLevels);

    }

    @Test
    public void addUserSkill() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenReturn(skill);

        UserSkill resultUserSkill = userSkillService.addUserSkill(user, addSkillRequest);

        Assert.assertEquals(testUserSkill.getId(), resultUserSkill.getId());

        Mockito.verify(userSkillRepository, Mockito.times(1)).save(any(UserSkill.class));
        Mockito.verify(skillService, Mockito.times(0)).addSkill(any());

    }

    @Test
    public void add_user_skill_as_well_as_new_skill() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenThrow(new SkillNotFoundException());
        Mockito.when(skillService.addSkill(addSkillRequest)).thenReturn(skill);
        UserSkill resultUserSkill = userSkillService.addUserSkill(user, addSkillRequest);

        Assert.assertEquals(testUserSkill.getId(), resultUserSkill.getId());

        Mockito.verify(userSkillRepository, Mockito.times(1)).save(any(UserSkill.class));
        Mockito.verify(skillService, Mockito.times(1)).addSkill(any());
    }

    @Test(expected = SkillAlreadyExistsException.class)
    public void if_skill_already_exist_exception_is_thrown() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(testUserSkill);

        userSkillService.addUserSkill(user, addSkillRequest);
    }

    @Test
    public void removeUserSkill() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(testUserSkill);

        UserSkill resultUserSkill = userSkillService.removeUserSkill(user.getId(), removeSkillRequest);

        Assert.assertEquals(testUserSkill.getId(), resultUserSkill.getId());

        Mockito.verify(userSkillRepository, Mockito.times(1)).delete(any(UserSkill.class));
    }

    @Test(expected = SkillNotFoundException.class)
    public void removing_not_existing_user_skill_throws_exception() throws Exception {
        Mockito.when(skillService.findByTitle(addSkillRequest.getTitle())).thenReturn(skill);
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(null);

        UserSkill resultUserSkill = userSkillService.removeUserSkill(user.getId(), removeSkillRequest);
    }

    @Test
    public void assignSkillLevel() throws Exception {
        Mockito.when(userSkillRepository.findByUserIdAndSkillId(
                user.getId(), assignSkillLevelRequest.getSkillId()))
                .thenReturn(testUserSkill);

        UserSkillLevel testUserSkillLevel = new UserSkillLevel(testUserSkill,
                new SkillLevel("Level title", "Level Description"));

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