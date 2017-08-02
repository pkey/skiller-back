package lt.swedbank.controllers.department;


import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.helpers.TestHelper;
import lt.swedbank.services.department.DepartmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DepartmentControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @InjectMocks
    private DepartmentController departmentController;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private org.springframework.validation.Validator mockValidator;

    @Autowired
    private ObjectMapper mapper;

    private Department department1;
    private Department department2;
    private List<Department> departments;
    private List<DepartmentEntityResponse> departmentResponses;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setValidator(mockValidator)
                .build();

        mapper = new ObjectMapper();

        departments = TestHelper.fetchDepartments(2);
        department1 = departments.get(0);
        department2 = departments.get(1);

        List<Team> teamList1 = new ArrayList<>();
        List<Team> teamList2 = new ArrayList<>();
        Team team1 = new Team("vienas", department1);
        Team team2 = new Team("du", department2);

        teamList1.add(team1);
        teamList2.add(team2);

        department1.setTeams(teamList1);
        department2.setTeams(teamList2);

        departmentResponses = new ArrayList<>();
        departmentResponses.add(new DepartmentEntityResponse(department1));
        departmentResponses.add(new DepartmentEntityResponse(department2));


    }


    @Test
    public void get_department_success() throws Exception {

        when(departmentService.getAllDepartmentEntityResponseList()).thenReturn(departmentResponses);

        mockMvc.perform(get("/departments/")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("pirmas")))
                .andExpect(jsonPath("$[0].teams", hasSize(1)))
                .andExpect(jsonPath("$[0].teams[0].name", is("vienas")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("antras")))
                .andExpect(jsonPath("$[1].teams", hasSize(1)))
                .andExpect(jsonPath("$[1].teams[0].name", is("du")));

        verify(departmentService, times(1)).getAllDepartmentEntityResponseList();

    }

}
