package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import org.springframework.data.repository.CrudRepository;


public interface UserSkillRepository extends CrudRepository<UserSkill, Long> {
    UserSkill findByUserIdAndSkillId(Long userId, Long skillId);
    Iterable<UserSkill> findBySkill(Skill skill);
}
