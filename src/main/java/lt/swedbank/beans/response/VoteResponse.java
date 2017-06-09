package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.Vote;

public class VoteResponse extends Response {

    private Long userId;
    private Long skillId;
    private String message;

    public VoteResponse(Vote vote) {
        this.userId = vote.getVoter().getId();
        this.skillId = vote.getUserSkillLevel().getUserSkill().getSkill().getId();
        this.message = vote.getMessage();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
