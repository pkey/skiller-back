package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentEntityResponse;
import lt.swedbank.beans.response.department.DepartmentOverviewResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.repositories.DepartmentRepository;
import lt.swedbank.services.team.TeamService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private TeamService teamService;

    private List<Department> departmentList;
    private Department testDepartment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        departmentList = TestHelper.fetchDepartments(2);
        testDepartment = departmentList.get(0);

    }

    private List<Department> generateDepartmentList() {

        List<Department> departmentsToBeGenerated = new ArrayList<>();
        int numberOfDepartments = 5;

        for (int i = 0; i < numberOfDepartments; i++) {
            Department department = generateDepartment(i);
            departmentsToBeGenerated.add(department);
        }

        return departmentsToBeGenerated;
    }

    private Department generateDepartment(int index) {
        Department department = new Department();
        department.setId((long) index);
        department.setName("Department No. " + index);

        return department;
    }

    @Test
    public void getAllDepartments() throws Exception {
        Mockito.when(departmentRepository.findAll()).thenReturn(departmentList);

        List<Department> resultDepartmentList = (List<Department>) departmentService.getAllDepartments();

        Assert.assertEquals(departmentList, resultDepartmentList);
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
        SkillTemplateResponse skillTemplateResponse1 = new SkillTemplateResponse(new SkillEntityResponse((long) 0, "Test Skill 1"),
                0,
                (double) 0);

        SkillTemplateResponse skillTemplateResponseSameAs1 = new SkillTemplateResponse(new SkillEntityResponse((long) 0, "Test Skill 1"),
                0,
                (double) 0);

        SkillTemplateResponse skillTemplateResponse2 = new SkillTemplateResponse(new SkillEntityResponse((long) 1, "Test Skill 2"),
                0,
                (double) 0);

        Set<SkillTemplateResponse> skillTemplateResponses1 = new HashSet<>();
        skillTemplateResponses1.add(skillTemplateResponse1);
        skillTemplateResponses1.add(skillTemplateResponse2);

        //Todo finish testing
        Mockito.when(teamService.getTeamSkillTemplateResponseList(any(Team.class))).thenReturn(skillTemplateResponses1, skillTemplateResponses1);

        Set<SkillTemplateResponse> skillTemplateResponses = departmentService.getDepartmentSkillTemplateResponses(testDepartment);
        Assert.assertEquals(skillTemplateResponses.size(), 10);
    }


}