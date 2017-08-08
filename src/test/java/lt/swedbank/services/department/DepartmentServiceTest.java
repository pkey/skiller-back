package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.SkillTemplate;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentEntityResponse;
import lt.swedbank.beans.response.department.DepartmentOverviewResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.DepartmentRepository;
import lt.swedbank.services.teamSkill.TeamSkillService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;


public class DepartmentServiceTest {

    @InjectMocks
    @Spy
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private TeamSkillService teamSkillService;

    private List<Department> departmentList;
    private Department testDepartment;

    private List<Team> testTeams;

    private SkillTemplate skillTemplate1;
    private SkillTemplate skillTemplate2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        departmentList = TestHelper.fetchDepartments();
        testDepartment = departmentList.get(0);

        skillTemplate1 = new SkillTemplate();
        skillTemplate1.addSkill(TestHelper.skills.get(0));

        skillTemplate2 = new SkillTemplate();
        skillTemplate2.addSkill(TestHelper.skills.get(1));

        Team testTeam1 = TestHelper.fetchTeams().get(0);
        Team testTeam2 = TestHelper.fetchTeams().get(1);

        testTeams = new ArrayList<>();
        testTeams.add(testTeam1);
        testTeams.add(testTeam2);
        testDepartment.setTeams(testTeams);



    }

    @Test
    public void getAllDepartmentEntityResponseList() throws Exception {
        Mockito.when(departmentRepository.findAll()).thenReturn(departmentList);

        List<DepartmentEntityResponse> resultDepartmentList
                = (List<DepartmentEntityResponse>) departmentService.getAllDepartmentEntityResponseList();

        Assert.assertThat(resultDepartmentList.size(), is(departmentList.size()));

    }

    @Test
    public void getDepartmentOverviewById() throws Exception {
        doReturn(testDepartment).when(departmentService).getDepartmentById(testDepartment.getId());
        doReturn(new HashSet<>()).when(departmentService).getUserResponsesFromDepartment(testDepartment);
        doReturn(new HashSet<>()).when(departmentService).aggregateDepartmentSkillTemplateResponsesFromTeams(testDepartment.getTeams());

        DepartmentOverviewResponse departmentOverviewResponse = departmentService.getDepartmentOverviewByDepartmentId(testDepartment.getId());

        Assert.assertEquals(departmentOverviewResponse.getDepartmentResponse().getId(), testDepartment.getId());
    }

    @Test
    public void getUserResponsesFromDepartment() throws Exception {
        Set<UserResponse> userResponses = departmentService.getUserResponsesFromDepartment(testDepartment);
        Assert.assertEquals(userResponses.size(), TestHelper.NUMBER_OF_USERS_IN_A_TEAM);
    }

    @Test
    public void departmentSkillTemplateResponsesAreDistinct() throws Exception {
        testTeams.get(0).setSkillTemplate(skillTemplate1);
        testTeams.get(1).setSkillTemplate(skillTemplate1);

        Mockito.when(teamSkillService.getTeamSkillCount(any(Team.class), any(Skill.class))).thenReturn(0, 0);
        Mockito.when(teamSkillService.getTeamAverageSkillLevel(any(Team.class), any(Skill.class))).thenReturn(0D, 0D);

        Set<SkillTemplateResponse> skillTemplateResponses = departmentService.aggregateDepartmentSkillTemplateResponsesFromTeams(testDepartment.getTeams());
        Assert.assertEquals(1, skillTemplateResponses.size());
    }

    @Test
    public void departmentSkillTemplateResponsesAreOrdered() throws Exception {
        testTeams.get(0).setSkillTemplate(skillTemplate1);
        testTeams.get(1).setSkillTemplate(skillTemplate2);

        int skillCounts[] = {2, 5};
        double averageSkillLevels[] = {1D, 3D};

        //Skill counts are returned exactly in the order specified.
        Mockito.when(teamSkillService.getTeamSkillCount(any(Team.class), any(Skill.class)))
                .thenReturn(skillCounts[0], skillCounts[1]);
        Mockito.when(teamSkillService.getTeamAverageSkillLevel(any(Team.class), any(Skill.class)))
                .thenReturn(averageSkillLevels[0], averageSkillLevels[0]);

        Set<SkillTemplateResponse> skillTemplateResponses = departmentService.aggregateDepartmentSkillTemplateResponsesFromTeams(testDepartment.getTeams());
        Assert.assertEquals(2, skillTemplateResponses.size());

        //Checks if order has changed
        Iterator<SkillTemplateResponse> iterator = skillTemplateResponses.iterator();
        for (int i = skillTemplateResponses.size() - 1; i >= 0; i--) {
            Assert.assertEquals(skillCounts[i], iterator.next().getUserCounter().intValue());
        }
    }
}