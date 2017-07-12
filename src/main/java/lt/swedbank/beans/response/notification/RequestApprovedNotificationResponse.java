package lt.swedbank.beans.response.notification;

import lt.swedbank.beans.entity.Approver;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.response.ApproverResponse;
import lt.swedbank.beans.response.SkillEntityResponse;

import java.util.ArrayList;
import java.util.List;


public class RequestApprovedNotificationResponse extends NotificationResponse {

    final private Integer type = 3;

    private Long id;

    private String message;

    private String senderName;

    private String senderLastname;

    private String skillLevel;

    private SkillEntityResponse skill;

    private List<ApproverResponse> approvals;

    public RequestApprovedNotificationResponse(){
    }

    public RequestApprovedNotificationResponse(RequestNotification requestNotification) {
        User user = requestNotification.getApprovalRequest().getUserSkillLevel().getUserSkill().getUser();
        UserSkillLevel userSkillLevel = requestNotification.getApprovalRequest().getUserSkillLevel();
        this.id = requestNotification.getId();
        this.skill = new SkillEntityResponse(userSkillLevel.getUserSkill().getSkill());
        this.message = requestNotification.getApprovalRequest().getMotivation();
        this.senderName = user.getName();
        this.senderLastname = user.getLastName();
        this.skillLevel = userSkillLevel.getSkillLevel().getTitle();
        this.approvals = new ArrayList<>();
        for(Approver approver : requestNotification.getApprovalRequest().getApprovers()) {
            this.approvals.add(new ApproverResponse(approver));
        }
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

    public List<ApproverResponse> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApproverResponse> approvals) {
        this.approvals = approvals;
    }
}