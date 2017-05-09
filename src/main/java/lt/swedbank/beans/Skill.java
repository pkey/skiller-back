package lt.swedbank.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity(name = "SKILL")
public class Skill {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private Long userID;

    public Skill() {}
    public Skill(String title, Long userID)
    {
        this.userID = userID;
        this.title = title;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }



    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}