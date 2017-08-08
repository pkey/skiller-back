package lt.swedbank.repositories;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.TeamSkill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamSkillRepository extends CrudRepository<TeamSkill, Long> {
    TeamSkill findTopByTeamAndSkill(Team team, Skill skill);
}
