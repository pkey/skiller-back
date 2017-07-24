package lt.swedbank.repositories;


import lt.swedbank.beans.entity.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
    Team findByName(String name);
}
