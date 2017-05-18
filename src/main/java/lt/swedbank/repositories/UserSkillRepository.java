package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import org.springframework.data.repository.CrudRepository;


public interface UserSkillRepository extends CrudRepository<UserSkill, Long> {
    UserSkill findByUserIDAndSkill(Long userid, Skill skill);
}
