package lt.swedbank.services.
        notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.response.RequestNotificationResponse;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
public class NotificationServiceTest {

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
    private UserSkillLevel userSkillLevel;

    @Before
    public void setUp()
    {
        requestNotificationResponses = new ArrayList<>();
        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification1));
        requestNotificationResponses.add(new RequestNotificationResponse(requestNotification2));
        user = new User();
        requestNotification1 = new RequestNotification();
        requestNotification2 = new RequestNotification();

        requestNotificationList = new LinkedList<>();
        requestNotificationList.add(requestNotification1);
        requestNotificationList.add(requestNotification2);

        userSkillLevel = new UserSkillLevel();
        userSkillLevel.setMotivation("test");
        userSkillLevel.setSkillLevel(new SkillLevel());
        approvalRequestl = new ApprovalRequest();
        approvalRequestl.setUserSkillLevel(userSkillLevel);
        approvalRequestl.setMotivation("test");

    }

    @Test
    public void getNotificationsByUserId()
    {
        Mockito.when(userService.getUserById(any())).thenReturn(user);
        Mockito.when(notificationService.getNotificationsByUserId(any())).thenReturn(requestNotificationList);
        assertEquals(notificationService.getNotificationsByUserId(any()), requestNotificationList);
    }

    @Test
    public void getRequestNotificationResponseTest()
    {
        assertEquals(requestNotificationResponses,(ArrayList<RequestNotificationResponse>) notificationService.getRequestNotificationResponse(requestNotificationList));
    }

    @Test
    public void approveByApprovalRequestIdTest(){
        doReturn(requestNotification1).when(notificationService).getNotificationById(any());
        Mockito.when(approvalService.approve(any(),any(),any())).thenReturn(approvalRequestl);
        assertEquals(notificationService.approveByApprovalRequestId(any(), any()), requestNotification1);



    }


}
