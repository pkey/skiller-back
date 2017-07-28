package lt.swedbank.repositories;


import lt.swedbank.beans.entity.ValueStream;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValueStreamRepository extends CrudRepository<ValueStream, Long> {
}
