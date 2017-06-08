package lt.swedbank.beans.entity;

import javax.persistence.*;

@Entity
public class RequestNotification  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User sender;
    @OneToOne
    private UserSkill userSkill;
    @OneToOne
    private Approval approval;


    public RequestNotification() {}

    public RequestNotification(User sender, UserSkill userSkill, Approval approval)
    {
        this.sender = sender;
        this.userSkill = userSkill;
        this.approval = approval;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public UserSkill getUserSkill() {
        return userSkill;
    }

    public void setUserSkill(UserSkill userSkill) {
        this.userSkill = userSkill;
    }

    public Approval getApproval() {
        return approval;
    }

    public void setApproval(Approval approval) {
        this.approval = approval;
    }

}
