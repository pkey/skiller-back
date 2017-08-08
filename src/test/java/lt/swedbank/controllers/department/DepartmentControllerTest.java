package lt.swedbank.controllers.department;


import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentEntityResponse;
import lt.swedbank.beans.response.department.DepartmentOverviewResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.user.UserResponse;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        departments = TestHelper.fetchDepartments();
        department1 = departments.get(0);
        department2 = departments.get(1);

        departmentResponses = new ArrayList<>();
        departmentResponses.add(new DepartmentEntityResponse(department1));
        departmentResponses.add(new DepartmentEntityResponse(department2));


    }


    @Test
    public void canGetDepartmentOverviewById() throws Exception {
        DepartmentResponse departmentResponse = new DepartmentResponse(department1);
        Set<UserResponse> userResponses = new HashSet<>();
        Set<SkillTemplateResponse> skillTemplateResponses = new HashSet<>();
        department1.getTeams().forEach(t -> t.getUsers().forEach(u -> userResponses.add(new UserResponse(u))));

        SkillEntityResponse skillEntityResponse = new SkillEntityResponse(TestHelper.skills.get(0));
        SkillTemplateResponse skillTemplateResponse = new SkillTemplateResponse(skillEntityResponse, 1, 1D);
        skillTemplateResponses.add(skillTemplateResponse);

        DepartmentOverviewResponse departmentOverviewResponse = new DepartmentOverviewResponse(departmentResponse,
                userResponses,
                skillTemplateResponses);

        when(departmentService.getDepartmentOverviewByDepartmentId(department1.getId())).thenReturn(departmentOverviewResponse);

        mockMvc.perform(get("/departments/" + department1.getId().toString())
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(department1.getId().intValue())))
                .andExpect(jsonPath("$.name", is(department1.getName())))

                .andExpect(jsonPath("$.users").isArray())
                .andExpect(jsonPath("$.users", hasSize(userResponses.size())))

                .andExpect(jsonPath("$.skillTemplate").isArray())
                .andExpect(jsonPath("$.skillTemplate", hasSize(skillTemplateResponses.size())));
    }

    @Test
    public void canGetAllDepartments() throws Exception {

        when(departmentService.getAllDepartmentEntityResponseList()).thenReturn(departmentResponses);

        mockMvc.perform(get("/departments/")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id", is(department1.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(department1.getName())))
                .andExpect(jsonPath("$[0].teams", hasSize(department1.getTeams().size())))
                .andExpect(jsonPath("$[0].teams[0].name", is(department1.getTeams().get(0).getName())))

                .andExpect(jsonPath("$[1].id", is(department2.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(department2.getName())))
                .andExpect(jsonPath("$[1].teams", hasSize(department2.getTeams().size())))
                .andExpect(jsonPath("$[1].teams[0].name", is(department2.getTeams().get(0).getName())));

        verify(departmentService, times(1)).getAllDepartmentEntityResponseList();

    }

}
