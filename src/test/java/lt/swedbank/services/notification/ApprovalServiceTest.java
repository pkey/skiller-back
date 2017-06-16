package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.services.skill.SkillLevelService;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class ApprovalServiceTest {

    @Spy
    @InjectMocks
    private ApprovalService approvalService;

    @Mock
    private SkillService skillService;

    @Mock
    private UserSkillService userSkillService;

    @Mock
    private SkillLevelService skillLevelService;

    @Mock
    private UserSkillLevelService userSkillLevelService;

    @Mock
    private ApprovalRequestRepository approvalRequestRepository;

    private ApprovalRequest testApprovalRequest;

    private UserSkill testUserSkill;
    private SkillLevel testSkillLevel;
    private Skill testSkill;
    private UserSkillLevel testUserSkillLevel;

    private List<User> testUserList;
    private List<RequestNotification> testNotificationsList;

    private User testUser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testUserList = TestHelper.fetchUsers(10);
        testNotificationsList = TestHelper.fetchRequestNotifications(10, testApprovalRequest);

        testUser = testUserList.get(0);

        testUserSkill = new UserSkill();
        testUserSkill.setId(Integer.toUnsignedLong(0));
        testUserSkill.setUser(testUser);
        testUserSkill.setSkill(new Skill("Test Skill"));

        testSkillLevel = new SkillLevel("Skill level 1", "description level 1");
        testUserSkillLevel = new UserSkillLevel(testUserSkill, testSkillLevel);

        testApprovalRequest = new ApprovalRequest();
        testApprovalRequest.setUserSkillLevel(testUserSkillLevel);
        testApprovalRequest.setMotivation("Approval request test motivation");
        testApprovalRequest.setRequestNotifications(testNotificationsList);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create_approval_request_success() {
        //UserSkillLevel testUserSkillLevel = mock(UserSkillLevel.class);
        //ApprovalRequest alreadyFoundApprovalRequest = mock(ApprovalRequest.class);

        Mockito.when(userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(any(), any())).thenReturn(any());
        Mockito.when(approvalRequestRepository.findByUserSkillLevel(any())).thenReturn(any());
        Mockito.when(skillLevelService.getAllByLevelGreaterThan(any())).thenReturn(any());
        Mockito.when(userSkillLevelService.getAllUserSkillLevelsSetBySkillLevels(any())).thenReturn(any());

        User user = userService.addUserSkill(testUser.getId(), testAddSkillRequest);
        assertEquals(user, testUser);

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(userSkillService, times(1)).addUserSkill(any(), any());
    }
}
