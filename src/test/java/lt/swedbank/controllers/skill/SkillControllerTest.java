package lt.swedbank.controllers.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.DepartmentEntityResponse;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.controllers.department.DepartmentController;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.services.department.DepartmentService;
import lt.swedbank.services.skill.SkillService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SkillControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private List<SkillEntityResponse> skills;


    @InjectMocks
    private SkillController skillController;

    @Mock
    private SkillService skillService;

    @Mock
    private org.springframework.validation.Validator mockValidator;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(skillController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setValidator(mockValidator)
                .build();

        mapper = new ObjectMapper();

        skills = new ArrayList<>();
        skills.add(new SkillEntityResponse(new Skill("Testing")));
        skills.add(new SkillEntityResponse(new Skill("Java")));
        skills.add(new SkillEntityResponse(new Skill("CSS")));
    }

    @Test
    public void get_skills_success() throws Exception {

        when(skillService.getSkillEntityResponseList()).thenReturn(skills);

        mockMvc.perform(get("/skill/all")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].title", is("Testing")))
                .andExpect(jsonPath("$[1].title", is("Java")))
                .andExpect(jsonPath("$[2].title", is("CSS")));

        verify(skillService, times(1)).getSkillEntityResponseList();

    }


}
