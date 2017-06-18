package lt.swedbank.beans.entity;

import lt.swedbank.exceptions.request.FalseRequestStatusException;

import javax.persistence.*;
import java.util.List;

@Entity
public class ApprovalRequest {

    private static final String APPROVED = "approved";
    private static final String DISAPPROVED = "disapproved";
    private static final String PENDING = "pending";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer approves = 0;

    private Integer isApproved = 0;

    @OneToOne(cascade = {CascadeType.ALL})
    private UserSkillLevel userSkillLevel;

    @OneToMany(cascade = {CascadeType.PERSIST})
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

    public void setRequestNotifications(List<RequestNotification> requestNotifications) {
        this.requestNotifications = requestNotifications;
    }

    public Integer getApproves() {
        return approves;
    }

    public void setApproves(Integer approves) {
        this.approves = approves;
    }

    public User getDisapprover() {
        return disapprover;
    }

    public void setDisapprover(User disapprover) {
        this.disapprover = disapprover;
    }

    public boolean removeNotification(RequestNotification requestNotification)
    {
        requestNotifications.remove(requestNotification);
        return true;
    }


    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Integer getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
        userSkillLevel.setIsApproved(isApproved);
    }

    public String getCurrentRequestStatus() {

        switch (isApproved) {
            case -1:
                return DISAPPROVED;
            case 0:
                return PENDING;
            case 1:
                return APPROVED;
            default:
                throw new FalseRequestStatusException();
        }

    }

}
