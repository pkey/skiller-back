package lt.swedbank.beans.entity;

import java.util.List;
import javax.persistence.*;

@Entity
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer approves = 0;

    private Integer isApproved = 0;

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

    public ApprovalRequest(List<RequestNotification> requestNotifications)
    {
        this.requestNotifications = requestNotifications;
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

    public Integer isApproved() {
        return isApproved;
    }

    public void setApproved(Integer approved) {
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

    public void setRequestNotifications(List<RequestNotification> requestNotifications) {
        this.requestNotifications = requestNotifications;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Integer approve()
    {
        if(isApproved == 0)
        {
            this.approves++;
            if(approves >= 5)
            {
                this.isApproved = 1;
            }
        }
        return approves;
    }

    public Integer disapprove() {
        if (isApproved == 0) {
            isApproved = -1;
            return 1;
        }
        return -1;
    }
}
