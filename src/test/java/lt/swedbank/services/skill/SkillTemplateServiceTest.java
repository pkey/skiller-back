package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillTemplate;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.SkillTemplateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;

public class SkillTemplateServiceTest {

    @InjectMocks
    private SkillTemplateService skillTemplateService;

    @Mock
    private SkillTemplateRepository skillTemplateRepository;

    private SkillTemplate skillTemplate;
    private Team team;
    private Team teamWithoutSkillTemplate;
    private List<Skill> skills;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        skillTemplate = new SkillTemplate(TestHelper.fetchTeams(1).get(0), TestHelper.fetchSkills(2));
        team = skillTemplate.getTeam();
        skills = skillTemplate.getSkills();
        teamWithoutSkillTemplate = team;
        teamWithoutSkillTemplate.setSkillTemplate(null);
    }

    @Test
    public void successfullyGettingTeamById() {
        Mockito.when(skillTemplateRepository.findOneByTeamId(any())).thenReturn(skillTemplate);

        Optional<SkillTemplate> skillTemplateResult = skillTemplateService.getByTeamId(skillTemplate.getId());

        Assert.assertEquals(skillTemplateResult.get(), skillTemplate);
    }

    @Test
    public  void creatingNewSkillTemplate() {
        Mockito.when(skillTemplateRepository.save(skillTemplate)).thenReturn(skillTemplate);

        SkillTemplate skillTemplateResult = skillTemplateService.createOrUpdateSkillTemplate(teamWithoutSkillTemplate, skills);

        Assert.assertEquals(skillTemplateResult, skillTemplate);
    }

    @Test
    public void updatingSkillTemplate() {
        Mockito.when(skillTemplateRepository.save(skillTemplate)).thenReturn(this.skillTemplate);

        team.setSkillTemplate(skillTemplate);
        SkillTemplate skillTemplateResult = skillTemplateService.createOrUpdateSkillTemplate(team, skills);

        Assert.assertEquals(skillTemplateResult, skillTemplate);

    }

}
