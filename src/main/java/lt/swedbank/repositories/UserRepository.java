package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    User findByAuthId(String authId);

    Iterable<User> findAllByOrderByNameAscLastNameAsc();

    Iterable<User> findAllByIdIsNotOrderByNameAscLastNameAsc(Long id);

    Iterable<User> findAllByTeam(Team team);
}
