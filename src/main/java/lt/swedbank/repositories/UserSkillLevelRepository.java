package lt.swedbank.repositories;

import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.logging.Level;

@Repository
public interface UserSkillLevelRepository extends CrudRepository<UserSkillLevel, Level> {
    UserSkillLevel findTopByUserSkillOrderByValidFromDesc(UserSkill userSkill);
}
