package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.exceptions.notification.ApproverNotFoundException;
import lt.swedbank.exceptions.notification.DisapproverNotFoundException;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.exceptions.userSkillLevel.RequestAlreadySubmittedException;
import lt.swedbank.exceptions.userSkillLevel.TooHighSkillLevelRequestException;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.ApprovalRequestRepository;
import lt.swedbank.repositories.ApproversRepository;
import lt.swedbank.repositories.DisaproversRepository;
import lt.swedbank.services.skill.SkillLevelService;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)

public class ApprovalServiceTest {

    @InjectMocks
    @Spy
    private ApprovalService approvalService;
    @Mock
    private ApproversRepository approversRepository;
    @Mock
    private DisaproversRepository disaproversRepository;
    @Mock
    private ApprovalRequestRepository approvalRequestRepository;
    @Mock
    private UserSkillLevelService userSkillLevelService;
    @Mock
    private UserSkillService userSkillService;
    @Mock
    private SkillLevelService skillLevelService;
    @Mock
    private UserService userService;

    private User user;
    private ApprovalRequest approvalRequest;
    private Approver approver;
    private List<Approver> approvers;
    private Disapprover disapprover;
    private List<Disapprover> disapprovers;
    private String message;
    private UserSkillLevel userSkillLevel;
    private RequestNotification requestNotification;
    private AssignSkillLevelRequest assignSkillLevelRequest;
    private UserSkill userSkill;
    private Skill skill;
    private List<SkillLevel> skillLevels;
    private Set<UserSkillLevel> userSkillLevels;
    private List<User> usersToBeNotifed;
    private String motivationMessage;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(approvalService, "APPROVES_NEEDED", 5);
        ReflectionTestUtils.setField(approvalService, "DISAPPROVES_NEEDED", 1);
        ReflectionTestUtils.setField(approvalService, "MIN_AMOUNT_OF_NOTIFIED_USERS_REQUIRED", 5);

        userSkillLevel = new UserSkillLevel();
        requestNotification = new RequestNotification();

        approvers = new ArrayList<>();
        disapprovers = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            user = new User();
            approver = new Approver();
            disapprover = new Disapprover();
            user.setName("AnyName" + i);
            approver.setUser(user);
            disapprover.setUser(user);
            approvers.add(approver);
            disapprovers.add(disapprover);
        }
        approvalRequest = new ApprovalRequest();
        approvalRequest.setApprovers(approvers);
        approvalRequest.setDisapprovers(disapprovers);
        approvalRequest.setUserSkillLevel(userSkillLevel);
        requestNotification.setApprovalRequest(approvalRequest);

        message = "Message";
        motivationMessage = "I am, i am";

        assignSkillLevelRequest = new AssignSkillLevelRequest(1L, 1L, motivationMessage);
        skill = new Skill(TestHelper.skills.get(0).getTitle());
        userSkill = new UserSkill(user, skill);
        userSkillLevel = new UserSkillLevel(userSkill, TestHelper.skillLevels.get(0));

        skillLevels = new ArrayList<>();
        skillLevels = TestHelper.skillLevels;
        Skill skill2 = new Skill(TestHelper.skills.get(1).getTitle());
        UserSkill userSkill2 = new UserSkill(user, skill2);
        UserSkillLevel userSkillLevel2 = new UserSkillLevel(userSkill2, TestHelper.skillLevels.get(1));

        userSkillLevels = new HashSet<>();
        userSkillLevels.add(userSkillLevel);
        userSkillLevels.add(userSkillLevel2);

        usersToBeNotifed = TestHelper.fetchUsers(3);

        userSkillLevel.getSkillLevel().setLevel(new Long(1));


    }

    @Test
    public void userAlreadyNotApprovedRequestTest() {
        user = new User();
        user.setName("Jonas");
        user.setId(1L);

        Assert.assertEquals(approvalService.isUserAlreadyApprovedReqest(user, approvalRequest), false);
    }



    @Test
    public void userAlreadyApprovedRequestTest() {
        user = approvalRequest.getApprovers().get(0).getUser();

        boolean isUserAlreadyApprovedRequestResult = approvalService.isUserAlreadyApprovedReqest(user, approvalRequest);

        Assert.assertEquals(isUserAlreadyApprovedRequestResult, true);
    }

    @Test
    public void saveApproverTest() {
        approver = approvalRequest.getApprovers().get(0);
        Mockito.when(approversRepository.save(approver)).thenReturn(approver);

        Approver approver1 = approvalService.saveApprover(approver);

        Assert.assertEquals(approver1, approver);
    }

    @Test
    public void userAlreadyDisapprovedRequestTest() {
        user = approvalRequest.getApprovers().get(0).getUser();

        boolean isUserAlreadyDisapprovedRequestResult = approvalService.isUserAlreadyDissapprovedRequest(user, approvalRequest);

        Assert.assertEquals(isUserAlreadyDisapprovedRequestResult, true);
    }

    @Test
    public void userAlreadyNotDisapprovedRequestTest() {
        user = new User();
        user.setName("Jonas");
        user.setId(1L);

        boolean isUserAlreadyDisapprovedRequestResult = approvalService.isUserAlreadyDissapprovedRequest(user, approvalRequest);

        Assert.assertEquals(isUserAlreadyDisapprovedRequestResult, false);
    }

    @Test(expected = UserNotFoundException.class)
    public void notRemoveDissapproverFromApprovalRequest() {
        Mockito.doNothing().when(disaproversRepository).delete(anyLong());

        approvalService.removeDissapproverFromApprovalRequest(null, approvalRequest);

        verify(disaproversRepository, times(0)).delete(anyLong());
    }

    @Test
    public void removeDissapproverFromApprovalRequest() {
        Mockito.doNothing().when(disaproversRepository).delete(anyLong());
        doReturn(new Disapprover()).when(approvalService).getDisapproverById(any());

        approvalService.removeDissapproverFromApprovalRequest(user, approvalRequest);

        verify(disaproversRepository, times(1)).delete(any(Disapprover.class));
    }

    @Test
    public void approveUserAlreadyDisapproved() {
        doReturn(true).when(approvalService).isUserAlreadyDissapprovedRequest(user, approvalRequest);
        Mockito.doNothing().when(approvalService).removeDissapproverFromApprovalRequest(user, approvalRequest);
        Mockito.when(approvalRequestRepository.save(approvalRequest)).thenReturn(approvalRequest);

        approvalService.approve(message, approvalRequest, user);

        verify(approvalService, times(1)).removeDissapproverFromApprovalRequest(user, approvalRequest);
    }

    @Test
    public void approveUserAlreadyApproved() {
        doReturn(false).when(approvalService).isUserAlreadyDissapprovedRequest(user, approvalRequest);
        Mockito.when(approvalRequestRepository.save(approvalRequest)).thenReturn(approvalRequest);
        doReturn(approver).when(approvalService).saveApprover(approver);
        doNothing().when(approvalService).removeUserFromApproversAndDisapproversIfExists(any(), any());

        User user = new User();
        user.setName("Approver");

        ApprovalRequest approvalRequestResult = approvalService.approve(message, approvalRequest, user);

        Assert.assertEquals(approvalRequestResult.getApprovers().get(approvalRequestResult.getApprovers().size() - 1).getUser(), user);
        Assert.assertEquals(approvalRequestResult.getApprovers().get(approvalRequestResult.getApprovers().size() - 1).getMessage(), message);
    }

    @Test
    public void saveDisapprover() {
        Mockito.when(disaproversRepository.save(disapprover)).thenReturn(disapprover);

        Disapprover disapproverResult = approvalService.saveDisapprover(disapprover);

        Assert.assertEquals(disapproverResult, disapprover);
    }

    @Test
    public void disapprove() {
        Mockito.when(approvalRequestRepository.save(approvalRequest)).thenReturn(approvalRequest);
        doReturn(disapprover).when(approvalService).saveDisapprover(disapprover);

        Disapprover disapprover = new Disapprover(user, message);
        doReturn(disapprover).when(approvalService).getDisapproverById(any());

        approvalRequest.setPending();
        ApprovalRequest approvalRequestResult = approvalService.disapprove(message, approvalRequest, user);

        Assert.assertEquals(approvalRequestResult.getDisapprovers().get(approvalRequestResult.getDisapprovers().size() - 1).getUser(), disapprover.getUser());
        Assert.assertEquals(approvalRequestResult.getDisapprovers().get(approvalRequestResult.getDisapprovers().size() - 1).getMessage(), disapprover.getMessage());
    }

    @Test
    public void getApprovalRequestByRequestNotification() {
        Mockito.when(approvalRequestRepository.findOne(requestNotification.getApprovalRequest().getId())).thenReturn(approvalRequest);

        ApprovalRequest approvalRequestResult = approvalService.getApprovalRequestByRequestNotification(requestNotification);

        Assert.assertEquals(approvalRequest, approvalRequestResult);
    }

    @Test
    public void update() {
        Mockito.when(approvalRequestRepository.save(approvalRequest)).thenReturn(approvalRequest);

        ApprovalRequest approvalRequestResult = approvalService.update(approvalRequest);

        Assert.assertEquals(approvalRequestResult, approvalRequest);
    }

    @Test
    public void addSkillLevelApprovalRequestWithNotifications() {
        Mockito.when(userSkillService.getUserSkillByUserIdAndSkillId(user.getId(), TestHelper.skillLevels.get(0).getId())).thenReturn(userSkill);
        Mockito.when(userSkillLevelService.addUserSkillLevel(any(), any())).thenReturn(userSkillLevel);
        Mockito.when(approvalRequestRepository.save(approvalRequest)).thenReturn(approvalRequest);
        Mockito.when(userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(user.getId(), assignSkillLevelRequest.getSkillId()))
                .thenReturn(userSkillLevel);

        Mockito.when(skillLevelService.getAllByLevelGreaterThanOrEqual(assignSkillLevelRequest.getLevelId())).thenReturn(skillLevels);
        Mockito.when(userSkillLevelService.getAllApprovedUserSkillLevelsBySkillLevels(skillLevels)).thenReturn(userSkillLevels);
        Mockito.when(userService.getAllUsers()).thenReturn(TestHelper.fetchUsers(3));

        assignSkillLevelRequest.setLevelId(new Long(2));

        ApprovalRequest approvalRequestResult = approvalService.addSkillLevelApprovalRequestWithNotifications(user.getId(), assignSkillLevelRequest);

        Assert.assertEquals(approvalRequestResult.getUserSkillLevel(), userSkillLevel);
        Assert.assertEquals(approvalRequestResult.getMotivation(), motivationMessage);
    }

    @Test(expected = RequestAlreadySubmittedException.class)
    public void requestAlreadySubmitedException() {
        Mockito.when(userSkillLevelService.isLatestUserSkillLevelPending(user.getId(), assignSkillLevelRequest.getSkillId())).thenReturn(true);

        approvalService.addSkillLevelApprovalRequestWithNotifications(user.getId(), assignSkillLevelRequest);
    }

    @Test(expected = TooHighSkillLevelRequestException.class)
    public void tooHighSkillLevelRequestException() {
        Mockito.when(userSkillLevelService.isLatestUserSkillLevelPending(user.getId(), assignSkillLevelRequest.getSkillId())).thenReturn(false);
        Mockito.when(userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(user.getId(), assignSkillLevelRequest.getSkillId()))
                .thenReturn(userSkillLevel);
        userSkillLevel.getSkillLevel().setLevel(new Long(0));
        assignSkillLevelRequest.setLevelId(new Long(2));


        approvalService.addSkillLevelApprovalRequestWithNotifications(user.getId(), assignSkillLevelRequest);
    }

    @Test
    public void getDisapproverById() {
        Disapprover disapprover = new Disapprover(user, "test");
        Mockito.doReturn(disapprover).when(disaproversRepository).findOne(any());
        Disapprover result = approvalService.getDisapproverById(any());
        Assert.assertEquals(disapprover, result);
    }

    @Test(expected = DisapproverNotFoundException.class)
    public void getDisapproverByIdException() {
        approvalService.getDisapproverById(null);
    }

    @Test
    public void getApproverById() {
        Approver disapprover = new Approver(user, "test");
        Mockito.doReturn(disapprover).when(approversRepository).findOne(any());
        Approver result = approvalService.getApproverById(any());
        Assert.assertEquals(disapprover, result);
    }

    @Test(expected = ApproverNotFoundException.class)
    public void getApproverByIdException() {
        approvalService.getApproverById(null);
    }
}
