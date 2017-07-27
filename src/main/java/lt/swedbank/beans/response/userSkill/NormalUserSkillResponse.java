package lt.swedbank.beans.response.userSkill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.entity.Vote;
import lt.swedbank.beans.response.CurrentSkillLevelResponse;
import lt.swedbank.beans.response.VoteResponse;

import java.util.ArrayList;
import java.util.List;

public class NormalUserSkillResponse extends UserSkillResponse {

    public NormalUserSkillResponse(Skill skill, UserSkillLevel userSkillLevel) {
        super(skill);
        this.setLevel(new CurrentSkillLevelResponse(userSkillLevel));
        this.setVotes(convertVoteEntityListToVoteResponseList(userSkillLevel.getVotes()));
    }
}
