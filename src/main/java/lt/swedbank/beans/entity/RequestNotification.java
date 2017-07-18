package lt.swedbank.beans.entity;


import lt.swedbank.exceptions.request.FalseRequestStatusException;


import javax.persistence.*;

@Entity
public class RequestNotification  {

    private static final String APPROVED = "approved";
    private static final String DISAPPROVED = "disapproved";
    private static final String PENDING = "pending";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isNewRequest = true;

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

    public void setApproved() {
        this.status = 1;
    }

    public void setDisapproved() {
        this.status = -1;
    }

    public void setPending() {
        this.status = 0;
    }

    public boolean isNewRequest() {
        return isNewRequest;
    }

    public void setNewRequest(boolean newRequest) {
        isNewRequest = newRequest;
    }

    public String getStatusAsString() {
        switch (status) {
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
