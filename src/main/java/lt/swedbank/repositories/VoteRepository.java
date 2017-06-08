package lt.swedbank.repositories;


import lt.swedbank.beans.entity.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Long>{
}
