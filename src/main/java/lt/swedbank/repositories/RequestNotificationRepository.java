package lt.swedbank.repositories;

import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface RequestNotificationRepository extends CrudRepository<RequestNotification, Long> {
    Iterable<RequestNotification> findByReceiver(User user);
}
