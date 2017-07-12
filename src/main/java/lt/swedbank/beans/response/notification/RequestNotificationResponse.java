package lt.swedbank.beans.response.notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.response.SkillEntityResponse;

public class RequestNotificationResponse extends NotificationResponse {

    final private Integer type = 1;

    private Long id;

    private String message;

    private String senderName;

    private String senderLastname;

    private String skillLevel;

    private SkillEntityResponse skill;

    public RequestNotificationResponse(){
    }

    public RequestNotificationResponse(RequestNotification requestNotification) {
        User user = requestNotification.getApprovalRequest().getUserSkillLevel().getUserSkill().getUser();
        UserSkillLevel userSkillLevel = requestNotification.getApprovalRequest().getUserSkillLevel();
        this.id = requestNotification.getId();
        this.skill = new SkillEntityResponse(userSkillLevel.getUserSkill().getSkill());
        this.message = requestNotification.getApprovalRequest().getMotivation();
        this.senderName = user.getName();
        this.senderLastname = user.getLastName();
        this.skillLevel = userSkillLevel.getSkillLevel().getTitle();
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