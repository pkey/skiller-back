package lt.swedbank.beans.response.userSkill;


import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkillLevel;

public class NonColleagueUserSkillResponse extends UserSkillResponse {

    public NonColleagueUserSkillResponse(Skill skill, UserSkillLevel userSkillLevel) {
        super(skill);
        this.setVotes(convertVoteEntityListToVoteResponseList(userSkillLevel.getVotes()));
    }

}
