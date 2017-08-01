package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillTemplate;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.exceptions.skillTemplate.TemplateNotFoundException;
import lt.swedbank.repositories.SkillTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class SkillTemplateService {

    @Autowired
    private SkillTemplateRepository skillTemplateRepository;

    private SkillTemplate getSkillTemplateById(@NotNull Long id) {
        SkillTemplate skillTemplate = skillTemplateRepository.findOne(id);

        if (skillTemplate == null) {
            throw new TemplateNotFoundException();
        }

        return skillTemplate;

    }

    public Optional<SkillTemplate> getByTeamId(@NotNull Long teamId) {
        return Optional.ofNullable(skillTemplateRepository.findOneByTeamId(teamId));
    }

    public SkillTemplate createSkillTemplate(Team team, List<Skill> skills) {
        SkillTemplate skillTemplate = new SkillTemplate(team, skills);
        return skillTemplateRepository.save(skillTemplate);
    }

    public SkillTemplate updateSkillTemplate(@NotNull Long id, @NotNull List<Skill> skills) {
        SkillTemplate skillTemplate = skillTemplateRepository.findOneByTeamId(id);
        skillTemplate.setSkills(skills);

        return skillTemplateRepository.save(skillTemplate);
    }
}
