package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillTemplate;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.exceptions.skillTemplate.TemplateNotFoundException;
import lt.swedbank.repositories.SkillTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    public SkillTemplate getByTeamId(@NotNull Long id) {
        SkillTemplate skillTemplate = skillTemplateRepository.findOneByTeamId(id);

        if (skillTemplate == null) {
            throw new TemplateNotFoundException();
        }

        return skillTemplate;
    }

    public SkillTemplate createSkillTemplate(Team team, List<Skill> skills) {
        SkillTemplate skillTemplate = new SkillTemplate(team, skills);
        return skillTemplateRepository.save(skillTemplate);
    }

    public SkillTemplate updateSkillTemplate(@NotNull Long id, @NotNull List<Skill> skills) {
        SkillTemplate skillTemplate = getSkillTemplateById(id);
        skillTemplate.setSkills(skills);

        return skillTemplateRepository.save(skillTemplate);
    }
}
