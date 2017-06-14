package lt.swedbank.beans.entity;

import java.util.List;
import javax.persistence.*;

@Entity
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer approves = 0;

    private boolean isApproved;

    @OneToOne
    private UserSkillLevel userSkillLevel;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<User> approvers;

    @OneToOne
    private User disapprover;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<RequestNotification> requestNotifications;

    private String motivation;

    public ApprovalRequest() {}

    public ApprovalRequest(List<RequestNotification> requestNotification)
    {
        this.requestNotifications = requestNotification;
    }

    public List<User> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<User> approvers) {
        this.approvers = approvers;
    }

    public void addApprover(User approver)
    {
        this.approvers.add(approver);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public UserSkillLevel getUserSkillLevel() {
        return userSkillLevel;
    }

    public void setUserSkillLevel(UserSkillLevel userSkillLevel) {
        this.userSkillLevel = userSkillLevel;
    }

    public List<RequestNotification> getRequestNotifications() {
        return requestNotifications;
    }

    public void setRequestNotification(List<RequestNotification> requestNotification) {
        this.requestNotifications = requestNotification;
    }

    public Integer getApproves() {
        return approves;
    }

    public User getDisapprover() {
        return disapprover;
    }

    public void setDisapprover(User disapprover) {
        this.disapprover = disapprover;
    }

    public void setApproves(Integer approves) {
        this.approves = approves;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Integer approve()
    {
        if(isApproved != false)
        {
            this.approves++;
        }
        return approves;
    }

    public Integer disapprove() {
        if (isApproved != true) {
            isApproved = false;
            return 1;
        }
        return -1;
    }
}
