package lt.swedbank.beans.response.userSkill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.Vote;
import lt.swedbank.beans.response.CurrentSkillLevelResponse;
import lt.swedbank.beans.response.VoteResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract public class UserSkillResponse  {
    protected Long id;
    protected String title;

    protected List<VoteResponse> votes;
    protected CurrentSkillLevelResponse level;

    public UserSkillResponse(Skill skill) {
        id = skill.getId();
        title = skill.getTitle();
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
        return votes.stream().map(VoteResponse::new).collect(Collectors.toList());
    }

    public List<VoteResponse> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteResponse> votes) {
        this.votes = votes;
    }

    public CurrentSkillLevelResponse getLevel() {
        return level;
    }

    public void setLevel(CurrentSkillLevelResponse level) {
        this.level = level;
    }

}
