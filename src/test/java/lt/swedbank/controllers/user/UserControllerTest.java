package lt.swedbank.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.beans.response.UserEntityResponse;
import lt.swedbank.handlers.RestResponseEntityExceptionHandler;
import lt.swedbank.services.auth.AuthenticationService;
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
import java.util.Date;
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
    private User correctUser2;
    private List<UserSkill> correctUserSkills;
    private List<UserSkill> correctUserSkills2;
    private UserSkill newlyAddedUserSkill;
    private List<Skill> correctSkills;
    private List<UserEntityResponse> correctUsers;
    private UserEntityResponse userEntityResponse;
    private UserEntityResponse userEntityResponse2;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationService authenticationService;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        mapper = new ObjectMapper();

        Long userId = Integer.toUnsignedLong(0);
        Long user2Id = Integer.toUnsignedLong(1);

        correctUser = new User();
        correctUser.setId(userId);
        correctUser.setName("TestUserName");
        correctUser.setLastName("TestUserLastName");
        correctUser.setPassword("TestUserPassword");
        correctUser.setEmail("testuser@gmail.com");

        UserSkill userSkill = new UserSkill();
        userSkill.setId(Integer.toUnsignedLong(0));
        userSkill.setUser(correctUser);
        userSkill.setSkill(new Skill("Test Skill"));

        UserSkillLevel userSkillLevel = new UserSkillLevel();
        userSkillLevel.setId(Integer.toUnsignedLong(0));
        userSkillLevel.setUserSkill(userSkill);
        userSkillLevel.setMotivation("Motyvacija");
        userSkillLevel.setValidFrom(new Date());

        SkillLevel skillLevel = new SkillLevel();
        skillLevel.setTitle("Test Default");
        skillLevel.setDescription("About Test Default");
        skillLevel.setLevel(Integer.toUnsignedLong(0));
        skillLevel.setId(Integer.toUnsignedLong(0));

        List<Vote> voteList= new ArrayList<>();
        Vote vote = new Vote();
        vote.setMessage("Voting message");
        vote.setId(Integer.toUnsignedLong(0));
        vote.setVoter(new User());
        vote.setUserSkillLevel(userSkillLevel);
        voteList.add(vote);

        userSkillLevel.setVotes(voteList);
        userSkillLevel.setSkillLevel(skillLevel);

        userSkill.setUserSkillLevel(userSkillLevel);

        correctUser.setUserSkill(userSkill);
        correctUsers = new ArrayList<>();



        userEntityResponse = new UserEntityResponse(correctUser);

        correctUsers.add(userEntityResponse);

        newlyAddedUserSkill = new UserSkill(correctUser, new Skill("SkillToAddLater"));
        newlyAddedUserSkill.setUserSkillLevel(userSkillLevel);

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void get_user_profile_success() throws Exception {
        when(userService.getUserProfile(Long.parseLong("0"))).thenReturn(userEntityResponse);

        mockMvc.perform(get("/user/profile/0")
                .header("Authorization", "Bearer")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.skills", hasSize(1)))
                .andExpect(jsonPath("$.skills[0].title", is("Test Skill")));



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
                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].name", is("TestUserName")))
                .andExpect(jsonPath("$[0].lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$[0].email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$[0].skills", hasSize(1)))
                .andExpect(jsonPath("$[0].skills[0].title", is("Test Skill")));


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
                .andExpect(jsonPath("$.skills", hasSize(1)))
                .andExpect(jsonPath("$.skills[0].title", is("Test Skill")));

        verify(userService, times(1)).getUserByAuthId(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void add_skill_to_user_success() throws Exception {

        String skillJson = mapper.writeValueAsString(new AddSkillRequest(newlyAddedUserSkill));

        List<UserSkill> tmpSkills = correctUser.getUserSkills();
        tmpSkills.add(newlyAddedUserSkill);
        correctUser.setUserSkills(tmpSkills);

        when(userService.getUserByAuthId(any())).thenReturn(correctUser);
        when(userService.addUserSkill(any(), any())).thenReturn(correctUser);
        when(userService.getUserById(any())).thenReturn(correctUser);

        mockMvc.perform(post("/user/skill/add").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.skills", hasSize(2)))
                .andExpect(jsonPath("$.skills[0].title", is("Test Skill")))
                .andExpect(jsonPath("$.skills[1].title", is("SkillToAddLater")));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).addUserSkill(any(), any());
        verify(userService, times(1)).getUserById(any());

        correctUser.setUserSkills(correctUserSkills);
    }

    @Test
    public void remove_skill_from_user_success() throws Exception {

        String skillJson = mapper.writeValueAsString(new RemoveSkillRequest(newlyAddedUserSkill));

        List<UserSkill> tmpSkills = correctUser.getUserSkills();
        tmpSkills.remove(0);
        correctUser.setUserSkills(tmpSkills);

        when(userService.getUserByAuthId(any())).thenReturn(correctUser);
        when(userService.removeUserSkill(any(), any())).thenReturn(newlyAddedUserSkill);
        when(userService.getUserById(any())).thenReturn(correctUser);

        mockMvc.perform(post("/user/skill/remove").header("Authorization", "Bearer")
                .contentType(contentType)
                .content(skillJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestUserName")))
                .andExpect(jsonPath("$.lastName", is("TestUserLastName")))
                .andExpect(jsonPath("$.email", is("testuser@gmail.com")))
                .andExpect(jsonPath("$.skills", hasSize(0)));

        verify(userService, times(1)).getUserByAuthId(any());
        verify(userService, times(1)).removeUserSkill(any(), any());
        verify(userService, times(1)).getUserById(any());

        correctUser.setUserSkills(correctUserSkills);
    }
}