package lt.swedbank.beans.request;


import javax.validation.constraints.NotNull;

public class VoteUserSkillRequest {

    @NotNull(message = "User ID is required!")
    private Long userId;

    @NotNull(message = "Skill ID is required!")
    private Long skillId;

    @NotNull(message = "Message is required")
    private String message;

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
