package lt.swedbank.beans.entity;

import javax.persistence.*;

@Entity
public class Disapprover {

    public Disapprover(){}

    public Disapprover(User user, String message)
    {
        this.message = message;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    private String message;
}
