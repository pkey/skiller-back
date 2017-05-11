package lt.swedbank.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RegisterUserRequest;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.user.UserService;
import org.junit.After;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    private User correctUser;
    private List<Skill> correctSkills;
    private Skill correctSkillToAddLater;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    @Mock
    private SkillService skillService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mapper = new ObjectMapper();

        Long userId = Integer.toUnsignedLong(0);

        correctUser = new User();
        correctUser.setId(userId);
        correctUser.setName("TestUserName");
        correctUser.setLastName("TestUserLastName");
        correctUser.setPassword("TestUserPassword");
        correctUser.setEmail("testuser@gmail.com");

        correctSkills = new ArrayList<>();
        correctSkills.add(new Skill("SkillName1", userId));
        correctSkills.add(new Skill("SkillName2", userId));
        correctSkills.add(new Skill("SkillName3", userId));

        correctUser.setSkills(correctSkills);

        correctSkillToAddLater = new Skill("SkillToAddLater", userId);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void get_user_success() throws Exception {

        when(userService.getUserByEmail(any())).thenReturn(correctUser);
        mockMvc.perform(get("/user/get").header("Authorization", "Bearer")
                                                .requestAttr("email", "a@a.a"))
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))//Irrelevant while skills array is hardcoded
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.skills", hasSize(3)))
                .andExpect(jsonPath("$.skills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$.skills[1].title", is("SkillName2")))
                .andExpect(jsonPath("$.skills[2].title", is("SkillName3")));
        verify(userService, times(1)).getUserByEmail(any());
        verifyNoMoreInteractions(userService);
    }

}