package lt.swedbank.services.vote;

import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.entity.Vote;
import lt.swedbank.beans.request.VoteUserSkillRequest;
import lt.swedbank.beans.response.VoteResponse;
import lt.swedbank.exceptions.vote.VoteForHimselfException;
import lt.swedbank.repositories.VoteRepository;
import lt.swedbank.services.skill.UserSkillLevelService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSkillLevelService userSkillLevelService;


    public VoteResponse voteUserSkill(VoteUserSkillRequest request, Long voterId) {
        User voter = userService.getUserById(voterId);

        if (voterId.equals(request.getUserId())) {
            throw new VoteForHimselfException();
        }

        UserSkillLevel userSkillLevel = userSkillLevelService
                .getCurrentUserSkillLevelByUserIdAndSkillId(request.getUserId(), request.getSkillId());

        Vote vote = new Vote(voter, userSkillLevel, request.getMessage());

        Vote savedVote = voteRepository.save(vote);

        return new VoteResponse(savedVote);

    }

}
