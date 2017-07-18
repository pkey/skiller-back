package lt.swedbank.beans.entity.log;


import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.UserSkillLevel;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
public class ApprovalRequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer approves = 0;

    private String status;

    private String userSkillLevel;

    private List<String> approvers;

    private String disapprover;

    private String motivation;

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RequestNotificationHistory> requestNotifications;

    public ApprovalRequestHistory() {}

    public ApprovalRequestHistory(ApprovalRequest)
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getApproves() {
        return approves;
    }

    public void setApproves(Integer approves) {
        this.approves = approves;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserSkillLevel() {
        return userSkillLevel;
    }

    public void setUserSkillLevel(String userSkillLevel) {
        this.userSkillLevel = userSkillLevel;
    }

    public List<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<String> approvers) {
        this.approvers = approvers;
    }

    public String getDisapprover() {
        return disapprover;
    }

    public void setDisapprover(String disapprover) {
        this.disapprover = disapprover;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public List<RequestNotificationHistory> getRequestNotifications() {
        return requestNotifications;
    }

    public void setRequestNotifications(List<RequestNotificationHistory> requestNotifications) {
        this.requestNotifications = requestNotifications;
    }
}
