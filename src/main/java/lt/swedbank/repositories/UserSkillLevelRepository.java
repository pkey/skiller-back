package lt.swedbank.repositories;

import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.enums.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.logging.Level;

@Repository
public interface UserSkillLevelRepository extends CrudRepository<UserSkillLevel, Level> {
    UserSkillLevel findTopByUserSkillAndStatusOrderByValidFromDesc(UserSkill userSkill, Status status);
    Iterable<UserSkillLevel> findAllBySkillLevelAndStatus(SkillLevel skillLevel, Status status);
}
