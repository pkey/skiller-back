package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.Vote;

public class VoteResponse extends Response {

    private Long voter;
    private Long skillId;
    private String message;

    public VoteResponse(Vote vote) {
        this.voter = vote.getVoter().getId();
        this.skillId = vote.getUserSkillLevel().getUserSkill().getSkill().getId();
        this.message = vote.getMessage();
    }

    public Long getVoter() {
        return voter;
    }

    public void setVoter(Long voter) {
        this.voter = voter;
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
