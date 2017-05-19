package lt.swedbank.beans.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private Long userID;

    @ManyToOne
    private Skill skill;

    public UserSkill(Long userID)
    {
        this.userID = userID;
    }

    public UserSkill(Long userID, Skill skill) {
        this.skill = skill;
        this.userID = userID;
    }

    public UserSkill() {}

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

    public String getTitle()
    {
        return skill.getTitle();
    }

}
