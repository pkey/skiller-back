package lt.swedbank.beans.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lt.swedbank.beans.enums.Status;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"approvers", "disapprovers", "requestNotifications"})
@ToString(exclude = {"approvers", "disapprovers", "requestNotifications"})
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Status status = Status.PENDING;

    @OneToOne(cascade = {CascadeType.ALL})
    private UserSkillLevel userSkillLevel;

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<Approver> approvers;

    @OneToMany(cascade = {CascadeType.PERSIST})
    private List<Disapprover> disapprovers;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<RequestNotification> requestNotifications;

    @CreationTimestamp
    private Date creationTime;

    private String motivation;

    public void addApprover(Approver approver) {
        if(approvers == null) {
            approvers = new ArrayList<>();
        }
        approvers.add(approver);
    }

    public void addDisapprover(Disapprover disapprover) {
        disapprovers.add(disapprover);
    }


    public void setRequestNotification(RequestNotification requestNotification) {
        this.requestNotifications = new ArrayList<RequestNotification>();
        this.requestNotifications.add(requestNotification);
    }

    public void setApproved() {
        this.status = Status.APPROVED;
        userSkillLevel.setApproved();
    }

    //Todo metodas nenaudojamas, naudojamas tik servisuose. Set pending kvieciamas tiesiogiai.
    public void setPending() {
        this.status = Status.PENDING;
        userSkillLevel.setPending();
    }

    public void setDisapproved() {
        this.status = Status.DISAPPROVED;
        userSkillLevel.setDisapproved();
    }
}
