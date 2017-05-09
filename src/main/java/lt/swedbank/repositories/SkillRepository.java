package lt.swedbank.repositories;

import lt.swedbank.beans.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SkillRepository extends CrudRepository<Skill, Long>{
}
