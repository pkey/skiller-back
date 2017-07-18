package lt.swedbank.beans.response.userSkill;


import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.Vote;
import lt.swedbank.beans.response.CurrentSkillLevelResponse;
import lt.swedbank.beans.response.VoteResponse;

import java.util.ArrayList;
import java.util.List;

public class NormalUserSkillResponse extends UserSkillResponse {


    protected CurrentSkillLevelResponse level;


    public NormalUserSkillResponse(UserSkill userSkill) {
        super(userSkill);
        level = new CurrentSkillLevelResponse(userSkill.getCurrentSkillLevel());
        votes = convertVoteEntityListToVoteResponseList(userSkill.getCurrentSkillLevel().getVotes());
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

}
