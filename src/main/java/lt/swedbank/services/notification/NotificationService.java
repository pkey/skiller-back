package lt.swedbank.services.notification;

import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.repositories.RequestNotificationRepository;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    RequestNotificationRepository requestNotificationRepository;
    @Autowired
    UserService userService;

    public Iterable<RequestNotification> getNotificationsByUserId(Long id)
    {
        return requestNotificationRepository.findByReceiverAndAnsweredFalse(userService.getUserById(id));
    }

}
