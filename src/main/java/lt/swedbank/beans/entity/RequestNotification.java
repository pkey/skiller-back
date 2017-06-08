package lt.swedbank.beans.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class RequestNotification  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User sender;

    @OneToOne
    private UserSkill userSkill;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Approval> approval;

    public RequestNotification() {}

    public RequestNotification(User sender, UserSkill userSkill, List<Approval> approval)
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

    public List<Approval> getApproval() {
        return approval;
    }

    public void setApproval(List<Approval> approval) {
        this.approval = approval;
    }

}
