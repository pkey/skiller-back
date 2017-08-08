package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        departmentList = TestHelper.fetchDepartments(2);
        testDepartment = departmentList.get(0);

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
        doReturn(new HashSet<>()).when(departmentService).getDepartmentSkillTemplateResponses(testDepartment);

        DepartmentOverviewResponse departmentOverviewResponse = departmentService.getDepartmentOverviewByDepartmentId(testDepartment.getId());

        Assert.assertEquals(departmentOverviewResponse.getDepartmentResponse().getId(), testDepartment.getId());
    }

    @Test
    public void getUserResponsesFromDepartment() throws Exception {
        Set<UserResponse> userResponses = departmentService.getUserResponsesFromDepartment(testDepartment);
        Assert.assertEquals(userResponses.size(), TestHelper.NUMBER_OF_USERS_IN_A_TEAM);
    }

    @Test
    public void getDepartmentSkillTemplateResponses() throws Exception {
        //TeamSkill teamSkill1 = new TeamSkill(testDepartment.getTeams().get(0));
        //Mockito.when(teamSkillService.getCurrentTeamSkillByTeamAndSkill(any(), any())).thenReturn();

        Set<SkillTemplateResponse> skillTemplateResponses = departmentService.getDepartmentSkillTemplateResponses(testDepartment);
        Assert.assertEquals(2, skillTemplateResponses.size());
    }


}