package lt.swedbank.beans.response.userSkill;


import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.Vote;
import lt.swedbank.beans.response.VoteResponse;

import java.util.ArrayList;
import java.util.List;

abstract public class UserSkillResponse {
    protected Long id;
    protected String title;

    public UserSkillResponse(UserSkill userSkill) {
        id = userSkill.getSkill().getId();
        title = userSkill.getSkill().getTitle();
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

    public List<VoteResponse> convertVoteEntityListToVoteResponseList(List<Vote> votes) {
        List<VoteResponse> voteResponses = new ArrayList<>();

        for (Vote vote : votes
                ) {
            voteResponses.add(new VoteResponse(vote));
        }

        return voteResponses;
    }
}
