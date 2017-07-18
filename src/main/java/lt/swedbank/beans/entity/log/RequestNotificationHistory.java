package lt.swedbank.beans.entity.log;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RequestNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date date;

    @ManyToOne
    private User sender;

    private String receiver;

    @ManyToOne
    private ApprovalRequestHistory approvalRequest;

    public RequestNotificationHistory() {}

    public RequestNotificationHistory(RequestNotification requestNotification) {
        this.date = requestNotification.getApprovalRequest().
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ApprovalRequestHistory getApprovalRequest() {
        return approvalRequest;
    }

    public void setApprovalRequest(ApprovalRequestHistory approvalRequest) {
        this.approvalRequest = approvalRequest;
    }
}
