package lt.swedbank.beans.entity;

import javax.persistence.*;

@Entity
public class RequestNotification  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private ApprovalRequest approvalRequest;

    private Boolean answered = false;

    public RequestNotification() {}


    public RequestNotification(User sender, ApprovalRequest approvalRequest)
    {
        this.receiver = sender;
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

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public Integer approve()
    {
        this.answered = true;
        return this.approvalRequest.approve();
    }
    public Integer disapprove()
    {
        this.answered = true;
        return this.approvalRequest.disapprove();
    }
}
