package lt.swedbank.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.AssignSkillLevelRequest;
import lt.swedbank.beans.request.AssignTeamRequest;
import lt.swedbank.beans.response.UserEntityResponse;
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
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private User correctUser2;
    private List<UserSkill> correctUserSkills;
    private List<UserSkill> correctUserSkills2;
    private UserSkill skill;
    private List<Skill> correctSkills;
    private List<UserEntityResponse> correctUsers;
    private UserEntityResponse userEntityResponse;
    private UserEntityResponse userEntityResponse2;
    private UserSkill correctUserSkill;
    private AssignSkillLevelRequest correctAssignSkillLevelRequest;
    private Team correctTeam;

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

        correctAssignSkillLevelRequest = new AssignSkillLevelRequest();
        correctAssignSkillLevelRequest.setMotivation("test");
        correctAssignSkillLevelRequest.setLevelId(Long.parseLong("1"));
        correctAssignSkillLevelRequest.setSkillId(Long.parseLong("1"));

        correctTeam = new Team();
        correctTeam.setName("Test");


        Long userId = Integer.toUnsignedLong(0);
        Long user2Id = Integer.toUnsignedLong(1);

        correctUser = new User();
        correctUser.setId(userId);
        correctUser.setName("TestUserName");
        correctUser.setLastName("TestUserLastName");
        correctUser.setPassword("TestUserPassword");
        correctUser.setEmail("testuser@gmail.com");

        correctUser2 = new User();
        correctUser2.setId(user2Id);
        correctUser2.setName("TestUserName2");
        correctUser2.setLastName("TestUserLastName2");
        correctUser2.setPassword("TestUserPassword2");
        correctUser2.setEmail("testuser2@gmail.com");

        correctSkills = new ArrayList<Skill>();
        correctSkills.add(new Skill("SkillName1"));
        correctSkills.add(new Skill("SkillName2"));
        correctSkills.add(new Skill("SkillName3"));

        correctUserSkills = new ArrayList<>();
        correctUserSkills.add(new UserSkill(correctUser, correctSkills.get(0)));
        correctUserSkills.add(new UserSkill(correctUser, correctSkills.get(1)));
        correctUserSkills.add(new UserSkill(correctUser, correctSkills.get(2)));

        correctUserSkills2 = new ArrayList<>();
        correctUserSkills2.add(new UserSkill(correctUser, correctSkills.get(0)));
        correctUserSkills2.add(new UserSkill(correctUser, correctSkills.get(1)));
        correctUserSkills2.add(new UserSkill(correctUser, correctSkills.get(2)));

        correctUserSkill = new UserSkill();
        correctUserSkill.setSkill(new Skill("testing"));

        correctUser.setUserSkills(correctUserSkills);
        correctUser2.setUserSkills(correctUserSkills2);

        correctUsers = new ArrayList<>();
        UserEntityResponse UserEntityResponse;

        userEntityResponse = new UserEntityResponse(correctUser);
        userEntityResponse2 = new UserEntityResponse(correctUser2);

        correctUsers.add(userEntityResponse);
        correctUsers.add(userEntityResponse2);

        skill = new UserSkill(correctUser, new Skill("SkillToAddLater"));

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void get_user_profile_success() throws Exception {
        when(userService.getUserProfile(Long.parseLong("1"))).thenReturn(userEntityResponse);

        mockMvc.perform(get("/user/profile/1")
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


        verify(userService, times(1)).getUserProfile(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void get_users_success() throws Exception {


        when(userService.getUserEntityResponseList()).thenReturn(correctUsers);

        mockMvc.perform(get("/user/all")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].name", is("TestUserName")))
                .andExpect(jsonPath("$[0].lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$[0].email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$[0].userSkills", hasSize(3)))
                .andExpect(jsonPath("$[0].userSkills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$[0].userSkills[1].title", is("SkillName2")))
                .andExpect(jsonPath("$[0].userSkills[2].title", is("SkillName3")))

                .andExpect(jsonPath("$[1].name", is("TestUserName2")))
                .andExpect(jsonPath("$[1].lastName", is("TestUserLastName2")))
                .andExpect(jsonPath("$[1].email", is("testuser2@gmail.com")))
                .andExpect(jsonPath("$[1].userSkills", hasSize(3)))
                .andExpect(jsonPath("$[1].userSkills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$[1].userSkills[1].title", is("SkillName2")))
                .andExpect(jsonPath("$[1].userSkills[2].title", is("SkillName3")));

        verify(userService, times(1)).getUserEntityResponseList();
        verifyNoMoreInteractions(userService);
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
    public void assignUserSkillLevel() throws Exception {

        String skillJson = mapper.writeValueAsString(correctAssignSkillLevelRequest);

        Mockito.when(authService.extractAuthIdFromToken(any())).thenReturn("TestAuth0Token");
        Mockito.when(userService.getUserByAuthId(any())).thenReturn(correctUser);
        Mockito.when(userService.assignUserSkillLevel(any(), any())).thenReturn(correctUserSkill);
        Mockito.when(userService.getUserById(any())).thenReturn(correctUser);

        correctUserSkills.get(0).setSkillLevel(new SkillLevel("PRO", "testing"));
        correctUser.setUserSkills(correctUserSkills);

        mockMvc.perform(post("/user/skill/level").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.userSkills", hasSize(3)))
                .andExpect(jsonPath("$.userSkills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$.userSkills[0].level.title", is("PRO")))
                .andExpect(jsonPath("$.userSkills[1].title", is("SkillName2")))
                .andExpect(jsonPath("$.userSkills[2].title", is("SkillName3")));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).assignUserSkillLevel(any(), any());
        verify(userService, times(1)).getUserById(any());
        verify(authService, times(1)).extractAuthIdFromToken(any());

        correctUserSkill.setSkillLevel(null);
        correctUser.setUserSkills(correctUserSkills);
    }



    @Test
    public void assignUserTeam() throws Exception {

        Mockito.when(authService.extractAuthIdFromToken(any())).thenReturn("TestAuth0Token");
        Mockito.when(userService.getUserByAuthId(any())).thenReturn(correctUser);
        Mockito.when(userService.assignTeam(any(), any())).thenReturn(correctUser);

        String skillJson = mapper.writeValueAsString(new AssignTeamRequest());

        correctUser.setTeam(correctTeam);

        mockMvc.perform(put("/user/team").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.team.name", is("Test")))
                .andExpect(jsonPath("$.userSkills", hasSize(3)))
                .andExpect(jsonPath("$.userSkills[0].title", is("SkillName1")))
                .andExpect(jsonPath("$.userSkills[1].title", is("SkillName2")))
                .andExpect(jsonPath("$.userSkills[2].title", is("SkillName3")));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).assignTeam(any(), any());
        verify(authService, times(1)).extractAuthIdFromToken(any());

        correctUser.setTeam(null);

    }

}