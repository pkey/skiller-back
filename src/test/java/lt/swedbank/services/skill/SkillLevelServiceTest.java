package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.response.SkillLevelResponse;
import lt.swedbank.repositories.SkillLevelRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;


public class SkillLevelServiceTest {

    public static int DEFAULT_SKILL_LEVEL = 1;

    @InjectMocks
    private SkillLevelService skillLevelService;

    @Mock
    private SkillLevelRepository skillLevelRepository;

    private List<SkillLevel> skillLevels;

    private SkillLevel testSkillLevel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        skillLevels = generateSkillLevelList();

        testSkillLevel = skillLevels.get(0);
    }

    private List<SkillLevel> generateSkillLevelList() {

        List<SkillLevel> skillLevelsToBeGenerated = new ArrayList<>();
        int overAllLevels = 5;

        for (int i = 0; i < overAllLevels; i++) {
            SkillLevel level = generateSkillLevel(i + 1);
            skillLevelsToBeGenerated.add(level);
        }

        return skillLevelsToBeGenerated;
    }

    private SkillLevel generateSkillLevel(int levelNo) {
        SkillLevel level = new SkillLevel();
        level.setId(Long.valueOf(levelNo - 1));
        level.setLevel(Long.valueOf(levelNo));
        level.setDescription("Description of level " + levelNo);
        level.setTitle("Level " + levelNo);

        return level;
    }


    @Test
    public void getAll() throws Exception {
        Mockito.when(skillLevelRepository.findAll()).thenReturn(skillLevels);

        List<SkillLevel> resultSkillLevels = (List<SkillLevel>) skillLevelService.getAll();

        Assert.assertEquals(skillLevels, resultSkillLevels);
    }

    @Test
    public void getByLevel() throws Exception {
        Mockito.when(skillLevelRepository.findByLevel(testSkillLevel.getLevel())).thenReturn(testSkillLevel);

        SkillLevel resultSkillLevel = skillLevelService.getByLevel(testSkillLevel.getLevel());

        Assert.assertEquals(resultSkillLevel, testSkillLevel);
    }

    @Test
    public void getById() throws Exception {
        Mockito.when(skillLevelRepository.findOne(testSkillLevel.getId())).thenReturn(testSkillLevel);

        SkillLevel resultSkillLevel = skillLevelService.getById(testSkillLevel.getId());

        Assert.assertEquals(testSkillLevel, resultSkillLevel);
    }

    @Test
    public void getDefault() throws Exception {
        Long defaultLevel = new Long(DEFAULT_SKILL_LEVEL);
        Mockito.when(skillLevelRepository.findByLevel(defaultLevel)).thenReturn(testSkillLevel);

        SkillLevel resultSkillLevel = skillLevelService.getDefault();

        Assert.assertEquals(testSkillLevel.getLevel(), resultSkillLevel.getLevel());
    }

    @Test
    public void getSkillLevelResponseList() {
        Mockito.when(skillLevelRepository.findAll()).thenReturn(skillLevels);

        List<SkillLevelResponse> resultSkillLevelResponseList = skillLevelService.getSkillLevelResponseList();

        Assert.assertThat(resultSkillLevelResponseList.size(), is(5));
    }

}