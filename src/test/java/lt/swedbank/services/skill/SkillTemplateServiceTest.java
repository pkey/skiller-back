package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillTemplate;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.SkillTemplateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class SkillTemplateServiceTest {

    @InjectMocks
    private SkillTemplateService skillTemplateService;

    @Mock
    private SkillTemplateRepository skillTemplateRepository;

    private SkillTemplate mockedSkillTemplate;
    private Team team;
    private List<Skill> skills;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockedSkillTemplate = new SkillTemplate(TestHelper.fetchTeams(1).get(0), TestHelper.fetchSkills(2));
        team = mockedSkillTemplate.getTeam();
        skills = mockedSkillTemplate.getSkills();
    }

    @Test
    public void successfullyGettingTeamById() {
        Mockito.when(skillTemplateRepository.findOneByTeamId(mockedSkillTemplate.getId())).thenReturn(mockedSkillTemplate);

        Optional<SkillTemplate> skillTemplateResult = skillTemplateService.getByTeamId(mockedSkillTemplate.getId());

        Assert.assertEquals(skillTemplateResult.get(), mockedSkillTemplate);
    }

    @Test
    public  void creatingNewSkillTemplate() {
        Mockito.when(skillTemplateRepository.save(mockedSkillTemplate)).thenReturn(mockedSkillTemplate);

        team.setSkillTemplate(null);
        SkillTemplate skillTemplateResult = skillTemplateService.createOrUpdateSkillTemplate(team, skills);

        Assert.assertEquals(skillTemplateResult, mockedSkillTemplate);
    }

    @Test
    public void updatingSkillTemplate() {
        Mockito.when(skillTemplateRepository.save(mockedSkillTemplate)).thenReturn(mockedSkillTemplate);

        team.setSkillTemplate(mockedSkillTemplate);
        SkillTemplate skillTemplateResult = skillTemplateService.createOrUpdateSkillTemplate(team, skills);

        Assert.assertEquals(skillTemplateResult, mockedSkillTemplate);

    }

}
