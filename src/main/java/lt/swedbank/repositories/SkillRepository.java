package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends CrudRepository<Skill, Long>{
    Skill findByTitleAndUserID(String title, Long userID);
}
