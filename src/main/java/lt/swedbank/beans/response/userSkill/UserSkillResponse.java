package lt.swedbank.beans.response.userSkill;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.Vote;
import lt.swedbank.beans.response.CurrentSkillLevelResponse;
import lt.swedbank.beans.response.Response;
import lt.swedbank.beans.response.VoteResponse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserSkillResponse extends Response {

     private Long id;
     private String title;
     private List<VoteResponse> votes;
     private CurrentSkillLevelResponse level;

    public UserSkillResponse(Skill skill) {
        id = skill.getId();
        title = skill.getTitle();
    }

    public List<VoteResponse> convertVoteEntityListToVoteResponseList(List<Vote> votes) {
        return votes.stream().map(VoteResponse::new).collect(Collectors.toList());
    }


}
