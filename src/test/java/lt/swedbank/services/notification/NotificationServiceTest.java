package lt.swedbank.services.
        notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.request.NotificationAnswerRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;
import lt.swedbank.exceptions.notification.NoSuchNotificationException;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
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
    private Skill skill1;

    @Before
    public void setUp() {
        notificationAnswerRequestl = new NotificationAnswerRequest();
        notificationAnswerRequestl.setMessage("test");
        notificationAnswerRequestl.setNotificationId(Long.parseLong("1"));

        user = new User();
        user.setName("test");
        user.setLastName("test");

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

        requestNotification1 = new RequestNotification();
        requestNotification2 = new RequestNotification();
        requestNotification1.setApprovalRequest(approvalRequestl);
        requestNotification2.setApprovalRequest(approvalRequestl);

        requestNotificationResponses = new ArrayList<>();
        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification1));
        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification2));
        user = new User();

        requestNotificationList = new LinkedList<>();
        requestNotificationList.add(requestNotification1);
        requestNotificationList.add(requestNotification2);
    }

    @Test
    public void getNotificationsByUserId() {
        Mockito.when(userService.getUserById(any())).thenReturn(user);
        Mockito.when(notificationService.getNotificationsByUserId(any())).thenReturn(requestNotificationList);
        assertEquals(notificationService.getNotificationsByUserId(any()), requestNotificationList);
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

}
