package lt.swedbank.repositories;


import lt.swedbank.beans.entity.unit.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long>
{
}
