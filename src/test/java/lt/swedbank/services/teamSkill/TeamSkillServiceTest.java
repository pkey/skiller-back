package lt.swedbank.services.teamSkill;

import lt.swedbank.beans.entity.*;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.TeamSkillRepository;
import lt.swedbank.services.skill.UserSkillService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TeamSkillServiceTest {

    @InjectMocks
    private TeamSkillService teamSkillService;
    @Mock
    private TeamSkillRepository teamSkillRepository;
    @Mock
    private UserSkillService userSkillService;

    @Captor
    private ArgumentCaptor<List<TeamSkill>> teamSkillListCaptor;
    @Captor
    private ArgumentCaptor<TeamSkill> teamSkillCaptor;

    private Team testTeam;
    private Skill testSkill;
    private TeamSkill teamSkill;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testTeam = TestHelper.fetchTeams().get(0);
        testSkill = new Skill("Test Skill");
        testSkill.setId(1L);

        SkillTemplate skillTemplate = new SkillTemplate(testTeam, new ArrayList<>());
        skillTemplate.addSkill(testSkill);

        testTeam.setSkillTemplate(skillTemplate);

        teamSkill = new TeamSkill(testTeam, testSkill, 1, 2D);
    }


    @Test
    public void create_team_skills_when_all_users_have_same_skill_and_level() throws Exception {
        UserSkillLevel userSkillLevel = new UserSkillLevel();
        userSkillLevel.setSkillLevel(TestHelper.skillLevels.get(0));
        Mockito.when(userSkillService.getCurrentSkillLevel(any())).thenReturn(userSkillLevel);

        //Set team members to contain testSkill
        testTeam.getUsers().forEach(u -> u.addUserSkill(new UserSkill(u, testSkill)));

        teamSkillService.createTeamSkills(testTeam);

        Mockito.verify(teamSkillRepository).save(teamSkillListCaptor.capture());
        TeamSkill teamSkill = teamSkillListCaptor.getValue().get(0);
        Assert.assertEquals(Integer.valueOf(5), teamSkill.getSkillCount());
        Assert.assertEquals(Double.valueOf(1), teamSkill.getSkillLevelAverage());

    }

    @Test
    public void update_team_skill() throws Exception {
        UserSkillLevel userSkillLevel = new UserSkillLevel();
        userSkillLevel.setSkillLevel(TestHelper.skillLevels.get(1));

        //Set team members to contain testSkill
        TeamSkill oldTeamSkill = new TeamSkill(testTeam, testSkill, 5, 2D);

        Mockito.when(userSkillService.getCurrentSkillLevel(any())).thenReturn(userSkillLevel);
        Mockito.when(teamSkillRepository.findTopByTeamAndSkill(any(), any())).thenReturn(oldTeamSkill);

        //Remove user skill from one team member
        User userToRemoveSkillFrom = testTeam.getUsers().get(0);
        userToRemoveSkillFrom.getUserSkills().remove(new UserSkill(userToRemoveSkillFrom, testSkill));

        teamSkillService.updateTeamSkill(testTeam, testSkill);

        Mockito.verify(teamSkillRepository).save(teamSkillCaptor.capture());

        TeamSkill updateTeamSkill = teamSkillCaptor.getValue();
        Assert.assertEquals(Integer.valueOf(4), updateTeamSkill.getSkillCount());
        Assert.assertEquals(Double.valueOf(2), updateTeamSkill.getSkillLevelAverage());
    }

    @Test
    public void canGetAverageCountOfTeamSkill() throws Exception {
        Mockito.when(teamSkillRepository.findTopByTeamAndSkill(testTeam, testSkill)).thenReturn(teamSkill);
        Integer skillCount = teamSkillService.getTeamSkillCount(testTeam, testSkill);
        Assert.assertEquals(teamSkill.getSkillCount(), skillCount);
    }

    @Test
    public void canGetAverageSkillLevelOfTeamSkill() throws Exception {
        Mockito.when(teamSkillRepository.findTopByTeamAndSkill(testTeam, testSkill)).thenReturn(teamSkill);
        Double averageSkillLevel = teamSkillService.getTeamAverageSkillLevel(testTeam, testSkill);
        Assert.assertEquals(teamSkill.getSkillLevelAverage(), averageSkillLevel);
    }
}