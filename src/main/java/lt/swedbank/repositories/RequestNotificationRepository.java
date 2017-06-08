package lt.swedbank.repositories;

import lt.swedbank.beans.entity.RequestNotification;
import org.springframework.data.repository.CrudRepository;

public interface RequestNotificationRepository extends CrudRepository<RequestNotification, Long> {
}
