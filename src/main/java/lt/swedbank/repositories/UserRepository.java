package lt.swedbank.repositories;

import lt.swedbank.beans.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
