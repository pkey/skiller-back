package lt.swedbank.beans.response.userSkill;


import lombok.Data;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.response.CurrentSkillLevelResponse;

@Data
public class NormalUserSkillResponse extends UserSkillResponse {

    public NormalUserSkillResponse(Skill skill, UserSkillLevel userSkillLevel) {
        super(skill);
        this.setLevel(new CurrentSkillLevelResponse(userSkillLevel));
        this.setVotes(convertVoteEntityListToVoteResponseList(userSkillLevel.getVotes()));
    }
}
