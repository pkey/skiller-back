package lt.swedbank.beans.entity;

import lt.swedbank.beans.enums.Status;
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer approves = 0;

    private Status status = Status.PENDING;

    @OneToOne(cascade = {CascadeType.ALL})
    private UserSkillLevel userSkillLevel;

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<Approver> approvers;

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<Disapprover> disapprovers;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<RequestNotification> requestNotifications;

    @CreationTimestamp
    private Date creationTime;

    private String motivation;

    public ApprovalRequest() {
    }

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

    public void addDisapprover(Disapprover disapprover) {
        disapprovers.add(disapprover);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void removeNotification(RequestNotification requestNotification) {
        requestNotifications.remove(requestNotification);
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Status getStatus() {
        return status;
    }

    public List<Disapprover> getDisapprovers() {
        return disapprovers;
    }

    public void setDisapprovers(List<Disapprover> disapprovers) {
        this.disapprovers = disapprovers;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setApproved() {
        this.status = Status.APPROVED;
        userSkillLevel.setApproved();
    }

    public void setPending() {
        this.status = Status.PENDING;
        userSkillLevel.setPending();
    }

    public void setDisapproved() {
        this.status = Status.DISAPPROVED;
        userSkillLevel.setDisapproved();
    }
}
