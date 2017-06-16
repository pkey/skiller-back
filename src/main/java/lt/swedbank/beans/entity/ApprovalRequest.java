package lt.swedbank.beans.entity;

import java.util.List;
import javax.persistence.*;

@Entity
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer isApproved = 0;

    @OneToOne
    private UserSkillLevel userSkillLevel;

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<ApprovalRequestAnswerers> approvalRequestaAnswerers;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<RequestNotification> requestNotifications;

    public ApprovalRequest() {}

    public ApprovalRequest(List<RequestNotification> requestNotifications)
    {
        this.requestNotifications = requestNotifications;
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

    public void setRequestNotifications(List<RequestNotification> requestNotifications) {
        this.requestNotifications = requestNotifications;
    }
    public boolean removeNotification(RequestNotification requestNotification)
    {
        requestNotifications.remove(requestNotification);
        return true;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public void awnser(ApprovalRequestAnswerers approvalRequestAnswerers) {
        this.approvalRequestaAnswerers.add(approvalRequestAnswerers);
    }

    public void approve()
    {
        this.isApproved = 1;
    }

    public void disapprove()
    {
        this.isApproved = -1;
    }
}
