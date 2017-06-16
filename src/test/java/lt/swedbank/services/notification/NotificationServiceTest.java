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

@RunWith(SpringJUnit4ClassRunner.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private RequestNotificationRepository requestNotificationRepository;
    @Mock
    private UserService userService;

    private RequestNotification requestNotification1;
    private RequestNotification requestNotification2;
    private List<RequestNotification> requestNotificationList;
    private List<RequestNotificationResponse> requestNotificationResponses;
    private User user;

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


}
