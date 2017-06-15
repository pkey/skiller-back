package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.repositories.DepartmentRepository;
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


public class DepartmentServiceTest {

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    private List<Department> departmentList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        departmentList = generateDepartmentList();
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
        department.setId(Long.valueOf(index));
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

}