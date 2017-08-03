package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.enums.Status;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.notification.NotificationResponse;
import lt.swedbank.beans.response.notification.RequestNotificationResponse;
import lt.swedbank.exceptions.notification.NoSuchNotificationException;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
public class NotificationServiceTest {

    @Spy
    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private RequestNotificationRepository requestNotificationRepository;
    @Mock
    private UserService userService;
    @Mock
    private ApprovalService approvalService;

    private RequestNotification requestNotification1;
    private RequestNotification requestNotification2;
    private List<RequestNotification> requestNotificationList;
    private List<RequestNotificationResponse> requestNotificationResponses;
    private User user;
    private ApprovalRequest approvalRequestl;
    private NotificationAnswerRequest notificationAnswerRequestl;
    private UserSkillLevel userSkillLevel;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(notificationService, "APPROVES_NEEDED", 5);
        ReflectionTestUtils.setField(notificationService, "DISAPPROVES_NEEDED", 1);

        LocalDateTime localDateTime = LocalDateTime.of(1111, 11, 11, 11, 11, 11);
        notificationAnswerRequestl = new NotificationAnswerRequest();
        notificationAnswerRequestl.setMessage("test");
        notificationAnswerRequestl.setNotificationId(1L);


        user = new User();
        user.setName("test");
        user.setLastName("test");
        user.setId(1L);

        Skill skill1 = new Skill("testing");
        UserSkill userSkill1 = new UserSkill(user, skill1);
        userSkill1.setUser(user);

        userSkillLevel = new UserSkillLevel();
        userSkillLevel.setUserSkill(userSkill1);
        userSkillLevel.setMotivation("test");
        userSkillLevel.setSkillLevel(new SkillLevel());

        approvalRequestl = new ApprovalRequest();
        approvalRequestl.setUserSkillLevel(userSkillLevel);
        approvalRequestl.setMotivation("test");
        approvalRequestl.setApprovers(new ArrayList<>());
        approvalRequestl.setDisapprovers(new ArrayList<>());

        requestNotification1 = new RequestNotification();
        requestNotification2 = new RequestNotification();
        requestNotification1.setApprovalRequest(approvalRequestl);
        requestNotification1.setId(1L);
        requestNotification1.setReceiver(user);

        requestNotification1.setCreationTime(localDateTime);
        requestNotification2.setApprovalRequest(approvalRequestl);
        requestNotification2.setCreationTime(localDateTime);

        requestNotificationResponses = new ArrayList<>();
        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification1));
        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification2));
        user = new User();

        requestNotificationList = new LinkedList<>();
        requestNotificationList.add(requestNotification1);
        requestNotificationList.add(requestNotification2);

    }

    @Test
    public void getNotificationsByUser() {
        Mockito.when(requestNotificationRepository.findByReceiver(any())).thenReturn(requestNotificationList);
        assertEquals(notificationService.getNotificationsByUser(any()), requestNotificationList);
    }


    @Test
    public void getNotificationByIdTest() {
        Mockito.when(requestNotificationRepository.findOne(any())).thenReturn(requestNotification1);
        assertEquals(notificationService.getNotificationById(any()), requestNotification1);
    }

    @Test(expected = NoSuchNotificationException.class)
    public void getNotificationByIdExcetionTest() {
        Mockito.when(requestNotificationRepository.findOne(any())).thenReturn(null);
        notificationService.getNotificationById(any());
    }

    @Test
    public void setNotificationAsExpired() {
        Mockito.when(requestNotificationRepository.save(requestNotificationList)).thenReturn(requestNotificationList);
        for (RequestNotification requestNotification : notificationService.setNotificationsAsExpired(requestNotificationList)
                ) {
            assertEquals(requestNotification.getStatus(), Status.EXPIRED);
        }
    }

    @Test
    public void handleRequestExpiredApproved() {
        doReturn(requestNotification1).when(notificationService).getNotificationById(notificationAnswerRequestl.getNotificationId());
        Mockito.when(approvalService.getApprovalRequestByRequestNotification(requestNotification1)).thenReturn(approvalRequestl);
        Mockito.when(requestNotificationRepository.save(any(RequestNotification.class))).thenReturn(requestNotification1);

        approvalRequestl.setApproved();
        NotificationResponse result = notificationService.handleRequest(notificationAnswerRequestl, user);
        assertEquals(result.getStatus(), Status.EXPIRED.toString());
        assertEquals(result.getType(), new Integer(3));
        assertEquals(requestNotification1.getStatus(), Status.EXPIRED);

    }

    @Test
    public void handleRequestExpiredDisapproved() {
        doReturn(requestNotification1).when(notificationService).getNotificationById(notificationAnswerRequestl.getNotificationId());
        Mockito.when(approvalService.getApprovalRequestByRequestNotification(requestNotification1)).thenReturn(approvalRequestl);
        Mockito.when(requestNotificationRepository.save(any(RequestNotification.class))).thenReturn(requestNotification1);

        approvalRequestl.setDisapproved();
        NotificationResponse result = notificationService.handleRequest(notificationAnswerRequestl, user);
        assertEquals(result.getStatus(), Status.EXPIRED.toString());
        assertEquals(result.getType(), new Integer(2));
        assertEquals(requestNotification1.getStatus(), Status.EXPIRED);
    }

    public void setUpMocks() {
        doReturn(requestNotification1).when(notificationService).getNotificationById(notificationAnswerRequestl.getNotificationId());
        Mockito.when(approvalService.getApprovalRequestByRequestNotification(requestNotification1)).thenReturn(approvalRequestl);
        Mockito.when(requestNotificationRepository.save(any(RequestNotification.class))).thenReturn(requestNotification1);
        Mockito.when(approvalService.approve(any(), any(), any())).thenReturn(approvalRequestl);
        Mockito.when(approvalService.disapprove(any(), any(), any())).thenReturn(approvalRequestl);
        doReturn(requestNotificationList).when(notificationService).setNotificationsAsExpired(any());
        Mockito.when(approvalService.update(any(ApprovalRequest.class))).thenReturn(approvalRequestl);
        Mockito.when(userService.getUserById(any())).thenReturn(user);
    }

    @Test
    public void handleRequestPendingApprove() {
        setUpMocks();
        approvalRequestl.setPending();
        notificationAnswerRequestl.setStatus(Status.APPROVED);
        requestNotification1.setApproved();
        NotificationResponse result = notificationService.handleRequest(notificationAnswerRequestl, user);
        assertEquals(result.getStatus(), Status.APPROVED.toString());
        assertEquals(approvalRequestl.getStatus(), Status.PENDING);
    }

    @Test
    public void handleRequestPendingDisapprove() {
        setUpMocks();
        approvalRequestl.setDisapprovers(new ArrayList<>());
        approvalRequestl.setPending();
        notificationAnswerRequestl.setStatus(Status.DISAPPROVED);
        NotificationResponse result = notificationService.handleRequest(notificationAnswerRequestl, user);
        assertEquals(Status.DISAPPROVED.toString(), result.getStatus());
        assertEquals(approvalRequestl.getStatus(), Status.PENDING);
    }
}
