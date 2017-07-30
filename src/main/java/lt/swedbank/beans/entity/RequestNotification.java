package lt.swedbank.beans.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lt.swedbank.beans.enums.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RequestNotification  {

    private Status status = Status.NEW;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NonNull
    private User receiver;

    @ManyToOne
    @NonNull
    private ApprovalRequest approvalRequest;

    @CreationTimestamp
    private LocalDateTime creationTime;

    public final void setApproved() {
        this.status = Status.APPROVED;
    }

    public final void setDisapproved() {
        this.status = Status.DISAPPROVED;
    }

    public final void setPending() {
        this.status = Status.PENDING;
    }

    public final void setExpired() { this.status = Status.EXPIRED; }

    public final String getStatusAsString() {
        return status.toString();
    }
}

