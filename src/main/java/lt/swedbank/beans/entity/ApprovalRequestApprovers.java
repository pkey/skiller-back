package lt.swedbank.beans.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class ApprovalRequestApprovers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max=500)
    private String message;

    @OneToOne
    private User approver;

    ApprovalRequestApprovers() {}

    ApprovalRequestApprovers(User approver, String message)
    {
        this.approver = approver;
        this.message = message;
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
