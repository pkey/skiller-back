package lt.swedbank.controllers.department;


import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
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
import java.util.*;

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

        private Department department1;
        private Department department2;
        private List<DepartmentEntityResponse> departments;


        @InjectMocks
        private DepartmentController departmentController;

        @Mock
        private DepartmentService departmentService;

        @Mock
        private org.springframework.validation.Validator mockValidator;

        @Autowired
        private ObjectMapper mapper;

        @Before
        public void setup() throws Exception {

            MockitoAnnotations.initMocks(this);

            mockMvc = MockMvcBuilders.standaloneSetup(departmentController)
                    .setControllerAdvice(new RestResponseEntityExceptionHandler())
                    .setValidator(mockValidator)
                    .build();

            mapper = new ObjectMapper();

            department1 = new Department();
            department1.setName("pirmas");
            department1.setId(Long.parseLong("1"));

            department2 = new Department();
            department2.setName("antras");
            department2.setId(Long.parseLong("2"));

            List<Team> teamList1 = new ArrayList<>();
            List<Team> teamList2 = new ArrayList<>();
            Team team1 = new Team();
            team1.setName("vienas");
            team1.setDepartment(department1);
            Team team2 = new Team();
            team2.setName("du");
            team2.setDepartment(department2);


            teamList1.add(team1);
            teamList2.add(team2);

            department1.setTeams(teamList1);
            department2.setTeams(teamList2);

            departments = new ArrayList<>();
            departments.add(new DepartmentEntityResponse(department1));
            departments.add(new DepartmentEntityResponse(department2));



        }


        @Test
        public void get_department_success() throws Exception {

            when(departmentService.getAllDepartments()).thenReturn(departments);

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

            verify(departmentService, times(1)).getAllDepartments();

        }

    }
