package lt.swedbank.beans.entity;


import lt.swedbank.beans.enums.Status;
import lt.swedbank.exceptions.request.FalseRequestStatusException;


import javax.persistence.*;

@Entity
public class RequestNotification  {

    private Status status = Status.pending;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isNewNotification = true;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private ApprovalRequest approvalRequest;

    public RequestNotification() {}

    public RequestNotification(User receiver, ApprovalRequest approvalRequest)
    {
        this.receiver = receiver;
        this.approvalRequest = approvalRequest;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public ApprovalRequest getApprovalRequest() {
        return approvalRequest;
    }

    public void setApprovalRequest(ApprovalRequest approvalRequest) {
        this.approvalRequest = approvalRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public final void setApproved() {
        this.status = Status.approved;
    }

    public final void setDisapproved() {
        this.status = Status.disapproved;
    }

    public final void setPending() {
        this.status = Status.pending;
    }

    public final void setExpired() { this.status = Status.expired; }

    public boolean isNewNotification() {
        return isNewNotification;
    }

    public void setNewNotification (boolean newNotification) {
        isNewNotification = newNotification;
    }

    public final String getStatusAsString() {
        return status.toString();
    }
}

