package lt.swedbank.controllers.skill;

import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.response.SkillLevelResponse;
import lt.swedbank.services.skill.SkillLevelService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SkillLevelControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @InjectMocks
    private SkillLevelController skillLevelController;

    @Mock
    private SkillLevelService skillLevelService;

    private MockMvc mockMvc;

    private List<SkillLevelResponse> skillLevelResponseList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(skillLevelController).build();

        skillLevelResponseList = new ArrayList<>();

        skillLevelResponseList.add(new SkillLevelResponse(new SkillLevel("Novice", "About Novice")));
        skillLevelResponseList.add(new SkillLevelResponse(new SkillLevel("Amateur", "About Amateur")));
        skillLevelResponseList.add(new SkillLevelResponse(new SkillLevel("Pro", "About Pro")));

    }

    @Test
    public void getSkillLevels() throws Exception {
        Mockito.when(skillLevelService.getSkillLevelResponseList()).thenReturn(skillLevelResponseList);

        mockMvc.perform(get("/skill/levels/all")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].title", is("Novice")))
                .andExpect(jsonPath("$[1].title", is("Amateur")))
                .andExpect(jsonPath("$[2].title", is("Pro")));
    }

}