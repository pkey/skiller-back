package lt.swedbank.beans.entity;


import lt.swedbank.exceptions.request.FalseRequestStatusException;


import javax.persistence.*;

@Entity
public class RequestNotification  {

    private static final String APPROVED = "approved";
    private static final String DISAPPROVED = "disapproved";
    private static final String PENDING = "pending";
    private static final String EXPIRED = "expired";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isNewNotification = true;

    private Integer status = 0;

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

    public Integer getStatus() {
        return status;
    }

    public final void setApproved() {
        this.status = 1;
    }

    public final void setDisapproved() {
        this.status = -1;
    }

    public final void setPending() {
        this.status = 0;
    }

    public final void setExpired() { this.status = 2; }

    public boolean isNewNotification() {
        return isNewNotification;
    }

    public void setNewNotification (boolean newNotification) {
        isNewNotification = newNotification;
    }

    public final String getStatusAsString() {
        switch (status) {
            case -1:
                return DISAPPROVED;
            case 0:
                return PENDING;
            case 1:
                return APPROVED;
            case 2:
                return EXPIRED;
            default:
                throw new FalseRequestStatusException();
        }
    }
}
