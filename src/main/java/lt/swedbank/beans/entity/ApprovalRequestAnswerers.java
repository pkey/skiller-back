package lt.swedbank.beans.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class ApprovalRequestAnswerers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ApprovalRequest approvalRequest;

    @Size(max=500)
    private String message;

    private Boolean approved;

    @OneToOne
    private User approver;

    public ApprovalRequestAnswerers() {}

    public ApprovalRequestAnswerers(User approver, String message, Boolean approved)
    {
        this.approver = approver;
        this.message = message;
        this.approved = approved;
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

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }
}
