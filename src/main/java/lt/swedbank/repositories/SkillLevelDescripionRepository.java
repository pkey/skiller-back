package lt.swedbank.repositories;

import lt.swedbank.beans.entity.SkillLevelDescription;
import org.springframework.data.repository.CrudRepository;

public interface SkillLevelDescripionRepository extends CrudRepository<SkillLevelDescription, Long>{
    SkillLevelDescription findByLevel(Long level);
}
