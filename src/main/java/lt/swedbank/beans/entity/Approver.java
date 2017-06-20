package lt.swedbank.beans.entity;

import javax.persistence.*;

@Entity
public class Approver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    @OneToOne
    private User user;

    private String message;

    public Approver() {}

    public Approver(User user, String message) {
        this.message = message;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
