package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.repositories.SkillRepository;
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


public class SkillServiceTest {

    @InjectMocks
    private SkillService skillService;

    @Mock
    private SkillRepository skillRepository;

    private AddSkillRequest addSkillRequest;

    private Skill testSkill;

    private List<Skill> testSkills;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testSkill = new Skill();
        testSkill.setTitle("Angular");

        addSkillRequest = new AddSkillRequest();
        addSkillRequest.setTitle("Angular");

        testSkills = new ArrayList<>();
        testSkills.add(testSkill);
        testSkills.add(testSkill);

    }

    @Test
    public void addSkill() throws Exception {
        Mockito.when(skillRepository.findByTitle(addSkillRequest.getTitle())).thenReturn(null);

        Skill resultSkill = skillService.addSkill(addSkillRequest);

        Assert.assertEquals(testSkill.getTitle(), resultSkill.getTitle());
    }

    @Test(expected = SkillAlreadyExistsException.class)
    public void if_skill_already_exists_exception_is_thrown() throws Exception {
        Mockito.when(skillRepository.findByTitle(addSkillRequest.getTitle())).thenReturn(testSkill);

        skillService.addSkill(addSkillRequest);
    }

    @Test
    public void findByTitle() throws Exception {
        Mockito.when(skillRepository.findByTitle(addSkillRequest.getTitle())).thenReturn(testSkill);

        Skill resultSkill = skillService.findByTitle(testSkill.getTitle());

        Assert.assertEquals(testSkill.getTitle(), resultSkill.getTitle());
    }

    @Test(expected = SkillNotFoundException.class)
    public void if_find_by_title_throws_not_found_if_skill_does_not_exist() throws Exception {
        Mockito.when(skillRepository.findByTitle(addSkillRequest.getTitle())).thenReturn(null);

        skillService.findByTitle(testSkill.getTitle());
    }

    @Test
    public void findById() throws Exception {
        Mockito.when(skillRepository.findByTitle(addSkillRequest.getTitle())).thenReturn(testSkill);

        Skill resultSkill = skillService.findByTitle(testSkill.getTitle());

        Assert.assertEquals(testSkill.getTitle(), resultSkill.getTitle());
    }

    @Test(expected = SkillNotFoundException.class)
    public void if_find_by_id_throws_not_found_if_skill_does_not_exist() throws Exception {
        Mockito.when(skillRepository.findOne(testSkill.getId())).thenReturn(null);

        skillService.findById(testSkill.getId());
    }

    @Test
    public void getAllSkills() throws Exception {
        Mockito.when(skillRepository.findAll()).thenReturn(testSkills);

        List<Skill> resultSkills = (List<Skill>) skillService.getAllSkills();

        Assert.assertEquals(testSkills, resultSkills);
    }

    @Test
    public void getSkillEntityResponseList() throws Exception {
        Mockito.when(skillRepository.findAll()).thenReturn(testSkills);

        List<SkillEntityResponse> resultSkillLevelResponseList = (List<SkillEntityResponse>) skillService.getSkillEntityResponseList();

        Assert.assertThat(resultSkillLevelResponseList.size(), is(testSkills.size()));
    }

}