package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.Vote;

import java.util.ArrayList;
import java.util.List;

public class UserSkillResponse {

    private Long id;
    private String title;

    private CurrentSkillLevelResponse level;

    private List<VoteResponse> votes;

    public UserSkillResponse(UserSkill userSkill) {
        id = userSkill.getSkill().getId();
        title = userSkill.getSkill().getTitle();
        level = new CurrentSkillLevelResponse(userSkill.getCurrentUserSkillLevel());
        votes = convertVoteEntityListToVoteResponseList(userSkill.getCurrentUserSkillLevel().getVotes());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CurrentSkillLevelResponse getLevel() {
        return level;
    }

    public void setLevel(CurrentSkillLevelResponse level) {
        this.level = level;
    }

    public List<VoteResponse> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteResponse> votes) {
        this.votes = votes;
    }

    public List<VoteResponse> convertVoteEntityListToVoteResponseList(List<Vote> votes) {
        List<VoteResponse> voteResponses = new ArrayList<>();

        for (Vote vote : votes
                ) {
            voteResponses.add(new VoteResponse(vote));
        }

        return voteResponses;
    }
}
