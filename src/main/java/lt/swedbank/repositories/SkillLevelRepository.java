package lt.swedbank.repositories;

import lt.swedbank.beans.entity.SkillLevel;
import org.springframework.data.repository.CrudRepository;

public interface SkillLevelRepository extends CrudRepository<SkillLevel, Long> {
    SkillLevel findByLevel(Long level);
}
