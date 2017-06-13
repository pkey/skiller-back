package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.*;

public class RequestNotificationResponse {

    final private Integer type = 1;

    private Long id;

    private String message;

    private String senderName;

    private String senderLastname;

    private String skillLevel;

    private SkillEntityResponse skill;

    public RequestNotificationResponse(Long id, User sender, Skill skill, String message, String skillLevel)
    {
        this.id = id;
        this.skill = new SkillEntityResponse(skill);
        this.message = message;
        this.senderLastname = sender.getLastName();
        this.senderName = sender.getName();
        this.skillLevel = skillLevel;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderLastname() {
        return senderLastname;
    }

    public void setSenderLastname(String senderLastname) {
        this.senderLastname = senderLastname;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public SkillEntityResponse getSkill() {
        return skill;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setSkill(SkillEntityResponse skill) {
        this.skill = skill;
    }

    public RequestNotificationResponse(){}
    public RequestNotificationResponse(RequestNotification requestNotification)
    {
        User user = requestNotification.getApprovalRequest().getUserSkillLevel().getUserSkill().getUser();
        this.id = requestNotification.getId();
        this.skill = new SkillEntityResponse(requestNotification.getApprovalRequest().getUserSkillLevel().getUserSkill().getSkill());
        this.message = requestNotification.getApprovalRequest().getUserSkillLevel().getMotivation();
        this.senderName = user.getName();
        this.senderLastname = user.getLastName();
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
            return id;
        }

    public void setId(Long id) {
            this.id = id;
        }

    public Integer getType() {
        return type;
    }
}
