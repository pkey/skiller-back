package lt.swedbank.beans.entity;

import java.util.List;
import javax.persistence.*;

@Entity
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;
    private Integer approves = 0;
    private boolean isApproved;

    @OneToOne
    private UserSkill userSkill;

    @OneToMany
    private List<User> approvers;

    @OneToMany
    private List<RequestNotification> requestNotification;

    public ApprovalRequest() {}

    public ApprovalRequest(List<RequestNotification> requestNotification)
    {
        this.requestNotification = requestNotification;
    }

    public List<User> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<User> approvers) {
        this.approvers = approvers;
    }

    public void addApproer(User approver)
    {
        this.approvers.add(approver);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public UserSkill getUserSkill() {
        return userSkill;
    }

    public void setUserSkill(UserSkill userSkill) {
        this.userSkill = userSkill;
    }

    public List<RequestNotification> getRequestNotification() {
        return requestNotification;
    }

    public void setRequestNotification(List<RequestNotification> requestNotification) {
        this.requestNotification = requestNotification;
    }

    public Integer getApproves() {
        return approves;
    }

    public void setApproves(Integer approves) {
        this.approves = approves;
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
