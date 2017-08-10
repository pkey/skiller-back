package lt.swedbank.beans.response.userSkill;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.response.CurrentSkillLevelResponse;

@Data
@EqualsAndHashCode(callSuper = false)
public class ColleagueUserSkillResponse extends UserSkillResponse {

    public ColleagueUserSkillResponse(Skill skill, UserSkillLevel userSkillLevel) {
        super(skill);
        this.setLevel(new CurrentSkillLevelResponse(userSkillLevel));
        this.setVotes(convertVoteEntityListToVoteResponseList(userSkillLevel.getVotes()));
    }
}
