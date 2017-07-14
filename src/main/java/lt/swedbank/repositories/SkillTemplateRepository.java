package lt.swedbank.repositories;

import lt.swedbank.beans.entity.SkillTemplate;
import lt.swedbank.beans.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillTemplateRepository extends CrudRepository<SkillTemplate, Long> {
    SkillTemplate findOneByTeam(Team team);
}
