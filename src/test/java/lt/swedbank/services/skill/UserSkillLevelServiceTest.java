package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.userSkillLevel.UserSkillLevelNotFoundException;
import lt.swedbank.repositories.UserSkillLevelRepository;
import lt.swedbank.services.notification.ApprovalService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

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
    private UserSkillLevel userSkillLevel;
    private AssignSkillLevelRequest assignSkillLevelRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userSkill = mock(UserSkill.class);

        userSkillLevel = mock(UserSkillLevel.class, Mockito.RETURNS_DEEP_STUBS);

        assignSkillLevelRequest = mock(AssignSkillLevelRequest.class);

    }

    @Test
    public void getCurrentUserSkillLevelByUserIdAndSkillId() throws Exception {
        Mockito.when(
                userSkillService.getUserSkillByUserIdAndSkillId(any(), any()))
                .thenReturn(userSkill);

        Mockito.when
                (userSkillLevelRepository.findTopByUserSkillAndStatusOrderByValidFromDesc(any(), any()))
                .thenReturn(userSkillLevel);

        UserSkillLevel resultUserSkillLevel = userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(any(), any());

        assertEquals(userSkillLevel, resultUserSkillLevel);
    }

    @Test(expected = UserSkillLevelNotFoundException.class)
    public void if_skill_level_does_not_exists_throw_exception(){

        Mockito.when(
                userSkillService.getUserSkillByUserIdAndSkillId(any(), any()))
                .thenReturn(userSkill);

        Mockito.when
                (userSkillLevelRepository.findTopByUserSkillAndStatusOrderByValidFromDesc(any(), any()))
                .thenReturn(null);

        userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(any(), any());

    }

    @Test
    public void addDefaultUserSkillLevel() throws Exception {
        ApprovalRequest approvalRequest = mock(ApprovalRequest.class);
        UserSkillLevel userSkillLevel = new UserSkillLevel();


        whenNew(UserSkillLevel.class).withArguments(any(UserSkill.class), any(SkillLevel.class)).thenReturn(userSkillLevel);

        Mockito.when(userSkillLevelRepository.save(any(UserSkillLevel.class))).thenReturn(userSkillLevel);

        UserSkillLevel resultUserSkillLevel = userSkillLevelService.addDefaultUserSkillLevel(userSkill);

        assertEquals(userSkillLevel.getId(), resultUserSkillLevel.getId());
    }

    @Test
    public void addUserSkillLevel() throws Exception {
        whenNew(UserSkillLevel.class).withAnyArguments().thenReturn(userSkillLevel);

        Mockito.when
                (userSkillLevelRepository.save(any(UserSkillLevel.class)))
                .thenReturn(userSkillLevel);

        UserSkillLevel resultUserSkillLevel = userSkillLevelService.addUserSkillLevel(userSkill, assignSkillLevelRequest);

        assertEquals(userSkillLevel, resultUserSkillLevel);
    }

}