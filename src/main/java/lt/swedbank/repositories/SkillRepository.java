package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends CrudRepository<Skill, Long> {
    Skill findByTitle(String title);

    Iterable<Skill> findSkillsById(List<Long> id);
}
