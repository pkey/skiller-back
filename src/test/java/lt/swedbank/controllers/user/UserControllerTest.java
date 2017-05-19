package lt.swedbank.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.exceptions.ApplicationException;
import lt.swedbank.exceptions.user.UserNotFoundException;
import lt.swedbank.beans.entity.User;
import lt.swedbank.handlers.ExceptionHandler;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.services.auth.AuthenticationService;
import lt.swedbank.services.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
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
    private List<UserSkill> correctUserSkills;
    private UserSkill skill;
    private List<Skill> correctSkills;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        Long userId = Integer.toUnsignedLong(0);

        correctUser = new User();
        correctUser.setId(userId);
        correctUser.setName("TestUserName");
        correctUser.setLastName("TestUserLastName");
        correctUser.setPassword("TestUserPassword");
        correctUser.setEmail("testuser@gmail.com");

        correctSkills = new ArrayList<Skill>();
        correctSkills.add(new Skill("SkillName1"));
        correctSkills.add(new Skill("SkillName2"));
        correctSkills.add(new Skill("SkillName3"));

        correctUserSkills = new ArrayList<>();
        correctUserSkills.add(new UserSkill(userId, correctSkills.get(0)));
        correctUserSkills.add(new UserSkill(userId, correctSkills.get(1)));
        correctUserSkills.add(new UserSkill(userId, correctSkills.get(2)));

        correctUser.setUserSkills(correctUserSkills);

        skill = new UserSkill(userId, new Skill("SkillToAddLater"));
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void get_user_success() throws Exception {


        when(userService.getUserByAuthId(any())).thenReturn(correctUser);

        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.userSkills", hasSize(3)))
                .andExpect(jsonPath("$.userSkills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$.userSkills[1].title", is("SkillName2")))
                .andExpect(jsonPath("$.userSkills[2].title", is("SkillName3")));

        verify(userService, times(1)).getUserByAuthId(any());
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void add_skill_to_user_success() throws Exception {

        String skillJson = mapper.writeValueAsString(new AddSkillRequest(skill));

        List<UserSkill> tmpSkills = new ArrayList<UserSkill>(correctUserSkills);
        tmpSkills.add(skill);
        correctUser.setUserSkills(tmpSkills);

        when(userService.getUserByAuthId(any())).thenReturn(correctUser);
        when(userService.addUserSkill(any(), any())).thenReturn(skill);
        when(userService.getUserById(any())).thenReturn(correctUser);

        mockMvc.perform(post("/user/skill/add").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.userSkills", hasSize(4)))
                .andExpect(jsonPath("$.userSkills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$.userSkills[1].title", is("SkillName2")))
                .andExpect(jsonPath("$.userSkills[2].title", is("SkillName3")))
                .andExpect(jsonPath("$.userSkills[3].title", is("SkillToAddLater")));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).addUserSkill(any(), any());
        verify(userService, times(1)).getUserById(any());

        correctUser.setUserSkills(correctUserSkills);
    }

    @Test
    public void remove_skill_from_user_success() throws Exception {

        String skillJson = mapper.writeValueAsString(new AddSkillRequest(correctUserSkills.get(2)));

        List<UserSkill> tmpSkills = new ArrayList<>(correctUserSkills);
        tmpSkills.remove(2);
        correctUser.setUserSkills(tmpSkills);

        when(userService.getUserByAuthId(any())).thenReturn(correctUser);
        when(userService.removeUserSkill(any(), any())).thenReturn(correctUserSkills.get(2));
        when(userService.getUserById(any())).thenReturn(correctUser);

        mockMvc.perform(post("/user/skill/remove").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.userSkills", hasSize(2)))
                .andExpect(jsonPath("$.userSkills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$.userSkills[1].title", is("SkillName2")));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).removeUserSkill(any(), any());
        verify(userService, times(1)).getUserById(any());

        correctUser.setUserSkills(correctUserSkills);
    }

    @Test
    public void test_if_user_not_found_is_thrown_when_it_is_not() throws Exception {

        Mockito.when(userService.getUserByAuthId(any())).thenThrow(new UserNotFoundException("Some error"));

        mockMvc.perform(get("/user/get")
                .header("Authorization", "Bearer fake_token")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

}