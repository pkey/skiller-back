package lt.swedbank.beans.entity;

import lt.swedbank.exceptions.request.FalseRequestStatusException;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
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
    private List<Approver> approvers;

    @OneToOne(cascade = {CascadeType.PERSIST})
    private Disapprover disapprover;

    @OneToMany(cascade = {CascadeType.ALL})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RequestNotification> requestNotifications;

    @CreationTimestamp
    private Date creationTime;

    private String motivation;

    public ApprovalRequest() {}

    public ApprovalRequest(List<RequestNotification> requestNotifications) {
        this.requestNotifications = requestNotifications;
    }

    public List<Approver> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<Approver> approvers) {
        this.approvers = approvers;
    }

    public void addApprover(Approver approver) {
        approvers.add(approver);
        approves++;
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

    public void setRequestNotification(RequestNotification requestNotification) {
        this.requestNotifications = new ArrayList<RequestNotification>();
        this.requestNotifications.add(requestNotification);
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

    public Disapprover getDisapprover() {
        return disapprover;
    }

    public void setDisapprover(Disapprover disapprover) {
        this.disapprover = disapprover;
    }

    public void removeNotification(RequestNotification requestNotification) {
        requestNotifications.remove(requestNotification);
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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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
