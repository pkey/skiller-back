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

    private SkillTemplate getById(@NotNull Long id) {
        SkillTemplate skillTemplate = skillTemplateRepository.findOne(id);

        if (skillTemplate == null) {
            throw new TemplateNotFoundException();
        }

        return skillTemplate;

    }

    public Optional<SkillTemplate> getByTeamId(@NotNull Long id) {
        return Optional.ofNullable(skillTemplateRepository.findOneByTeamId(id));
    }

    public SkillTemplate createOrUpdateSkillTemplate(@NotNull Team team, @NotNull List<Skill> skills) {
        return Optional.ofNullable(team.getSkillTemplate()).isPresent() ? updateSkillTemplate(team, skills) : createSkillTemplate(team, skills);
    }

    private SkillTemplate createSkillTemplate(Team team, List<Skill> skills) {
        return  saveSkillTemplate(new SkillTemplate(team, skills));
    }

    private SkillTemplate updateSkillTemplate(Team team, List<Skill> skills) {
        SkillTemplate skillTemplate = team.getSkillTemplate();
        skillTemplate.setSkills(skills);
        team.setSkillTemplate(skillTemplate);
        return saveSkillTemplate(team.getSkillTemplate());
    }

    private SkillTemplate saveSkillTemplate(SkillTemplate skillTemplate) {
        return skillTemplateRepository.save(skillTemplate);
    }
}
