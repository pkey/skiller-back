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

import static org.mockito.Mockito.mock;

public class SkillTemplateServiceTest {

    @InjectMocks
    private SkillTemplateService skillTemplateService;

    @Mock
    private SkillTemplateRepository skillTemplateRepository;

    private SkillTemplate mockedSkillTemplate;
    private Team mockedTeam;
    private List<Skill> mockedSkills;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockedSkillTemplate = new SkillTemplate(TestHelper.fetchTeams(1).get(0), TestHelper.fetchSkills(2));
        mockedTeam = mockedSkillTemplate.getTeam();
        mockedSkills = mockedSkillTemplate.getSkills();
    }

    @Test
    public void successfullyGettingTeamById() {
        Mockito.when(skillTemplateRepository.findOneByTeamId(mockedSkillTemplate.getId())).thenReturn(mockedSkillTemplate);

        Optional<SkillTemplate> skillTemplateResult = skillTemplateService.getSkillTemplateByTeamId(mockedSkillTemplate.getId());

        Assert.assertEquals(skillTemplateResult.get(), mockedSkillTemplate);
    }

    @Test
    public  void creatingNewSkillTemplate() {
        Mockito.when(skillTemplateRepository.save(mockedSkillTemplate)).thenReturn(mockedSkillTemplate);
        Team team1 = mock(Team.class);
        Mockito.when(team1.getSkillTemplate()).thenReturn(null);

        SkillTemplate skillTemplateResult = skillTemplateService.createOrUpdateSkillTemplate(mockedTeam, mockedSkills);

        Assert.assertEquals(skillTemplateResult, mockedSkillTemplate);
    }

    @Test
    public void updatingSkillTemplate() {
        Mockito.when(skillTemplateRepository.save(mockedSkillTemplate)).thenReturn(mockedSkillTemplate);
        Team team1 = mock(Team.class);
        Mockito.when(team1.getSkillTemplate()).thenReturn(mockedSkillTemplate);

        SkillTemplate skillTemplateResult = skillTemplateService.createOrUpdateSkillTemplate(mockedTeam, mockedSkills);

        Assert.assertEquals(skillTemplateResult, mockedSkillTemplate);

    }

}
