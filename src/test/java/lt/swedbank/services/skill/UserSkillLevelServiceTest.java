package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.userSkillLevel.UserSkillLevelNotFoundException;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.UserSkillLevelRepository;
import lt.swedbank.services.notification.ApprovalService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserSkillLevelServiceTest {

    @InjectMocks
    private UserSkillLevelService userSkillLevelService;

    @Mock
    private UserSkillLevelRepository userSkillLevelRepository;
    @Mock
    private SkillLevelService skillLevelService;
    @Mock
    private UserSkillService userSkillService;
    @Mock
    private ApprovalService approvalService;

    private UserSkill userSkill;
    private List<User> users;
    private UserSkillLevel userSkillLevel;
    private AssignSkillLevelRequest assignSkillLevelRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        users = TestHelper.fetchUsers(10);
        userSkill = users.get(0).getUserSkills().get(0);
        userSkillLevel = users.get(0).getUserSkills().get(0).getUserSkillLevels().get(0);

        assignSkillLevelRequest = new AssignSkillLevelRequest(3L, TestHelper.skills.get(0).getId(), "Improved");

    }

    @Test
    public void getCurrentUserSkillLevelByUserIdAndSkillId() throws Exception {
        when(userSkillService.getUserSkillByUserIdAndSkillId(any(), any())).thenReturn(userSkill);
        when(userSkillLevelRepository.findTopByUserSkillAndStatusOrderByValidFromDesc(any(), any())).thenReturn(userSkillLevel);

        UserSkillLevel resultUserSkillLevel = userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(any(), any());

        assertEquals(userSkillLevel, resultUserSkillLevel);
    }

    @Test(expected = UserSkillLevelNotFoundException.class)
    public void if_skill_level_does_not_exists_throw_exception() {
        when(userSkillService.getUserSkillByUserIdAndSkillId(any(), any())).thenReturn(userSkill);
        when(userSkillLevelRepository.findTopByUserSkillAndStatusOrderByValidFromDesc(any(), any())).thenReturn(null);

        userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(any(), any());

    }

    @Test
    public void addDefaultUserSkillLevel() throws Exception {
        when(skillLevelService.getDefault()).thenReturn(TestHelper.defaultSkillLevel);

        when(userSkillLevelRepository.save(any(UserSkillLevel.class))).thenReturn(userSkillLevel);

        UserSkillLevel resultUserSkillLevel = userSkillLevelService.addDefaultUserSkillLevel(userSkill);
        
        assertEquals(userSkillLevel.getUserSkill().getId(), resultUserSkillLevel.getUserSkill().getId());
    }

    @Test
    public void addUserSkillLevel() throws Exception {
        when(skillLevelService.getByLevel(assignSkillLevelRequest.getLevelId())).thenReturn(TestHelper.skillLevels.get(2));
        when(userSkillLevelRepository.save(any(UserSkillLevel.class))).thenReturn(userSkillLevel);

        UserSkillLevel resultUserSkillLevel = userSkillLevelService.addUserSkillLevel(userSkill, assignSkillLevelRequest);

        assertEquals(userSkillLevel, resultUserSkillLevel);
    }

}