package lt.swedbank.services.vote;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.VoteUserSkillRequest;
import lt.swedbank.beans.response.VoteResponse;
import lt.swedbank.exceptions.vote.VoteForHimselfException;
import lt.swedbank.repositories.VoteRepository;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;


public class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private UserService userService;

    @Mock
    private UserSkillLevelService userSkillLevelService;

    @Mock
    private VoteRepository voteRepository;

    private User voter;

    private User skillOwner;

    private Skill skill;

    private UserSkillLevel userSkillLevel;

    private Vote vote;

    private VoteUserSkillRequest voteUserSkillRequest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        voter = new User();
        voter.setId(new Long(0));
        voter.setName("Voter");

        skillOwner = new User();
        skillOwner.setId(new Long(1));
        skillOwner.setName("Owner");

        skill = new Skill("testSkill");
        skill.setId(new Long(0));

        userSkillLevel = new UserSkillLevel();//mock(UserSkillLevel.class);
        userSkillLevel.setUserSkill(new UserSkill(skillOwner, skill));

        voteUserSkillRequest = new VoteUserSkillRequest();
        voteUserSkillRequest.setUserId(skillOwner.getId());
        voteUserSkillRequest.setMessage("He is too good");
        voteUserSkillRequest.setSkillId(skill.getId());

        vote = new Vote();
        vote.setId(new Long(0));
        vote.setVoter(voter);
        vote.setUserSkillLevel(userSkillLevel);
        vote.setMessage(voteUserSkillRequest.getMessage());
    }

    @Test
    public void voteUserSkill() throws Exception {
        Mockito.when(userService.getUserById(voter.getId())).thenReturn(voter);
        Mockito.when(userSkillLevelService.getCurrentUserSkillLevelByUserIdAndSkillId(
                voteUserSkillRequest.getUserId(), voteUserSkillRequest.getSkillId()))
                .thenReturn(userSkillLevel);
        Mockito.when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        VoteResponse resultResponse = voteService.voteUserSkill(voteUserSkillRequest, voter.getId());

        Mockito.verify(voteRepository, Mockito.times(1));
    }

    @Test(expected = VoteForHimselfException.class)
    public void cannot_vote_for_oneself() {
        Mockito.when(userService.getUserById(skillOwner.getId())).thenReturn(skillOwner);

        voteService.voteUserSkill(voteUserSkillRequest, skillOwner.getId());

    }

}