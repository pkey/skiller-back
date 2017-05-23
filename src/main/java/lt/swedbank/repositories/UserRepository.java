package lt.swedbank.repositories;

import com.auth0.json.mgmt.Page;
import lt.swedbank.beans.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByAuthId(String authId);
    Iterable<User> findAllByOrderByNameAscLastNameAsc();
}
