package lt.swedbank.beans.entity;

import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;

@Entity
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    @OneToOne
    private User approver;

    @OneToOne
    private RequestNotification requestNotification;

    public Approval() {}

    public Approval(User approver, RequestNotification requestNotification)
    {
        this.approver = approver;
        this.requestNotification = requestNotification;
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

    public RequestNotification getRequestNotification() {
        return requestNotification;
    }

    public void setRequestNotification(RequestNotification requestNotification) {
        this.requestNotification = requestNotification;
    }
}
