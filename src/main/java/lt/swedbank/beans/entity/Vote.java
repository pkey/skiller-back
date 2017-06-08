package lt.swedbank.beans.entity;

import javax.persistence.*;

@Entity
@Table(
        name = "vote",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"voter_id", "user_skill_level_id" })
        }
)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User voter;

    @ManyToOne
    private UserSkillLevel userSkillLevel;

    private String message;

    public Vote() {
    }

    public Vote(User voter, UserSkillLevel userSkillLevel, String message) {
        this.voter = voter;
        this.userSkillLevel = userSkillLevel;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getVoter() {
        return voter;
    }

    public void setVoter(User voter) {
        this.voter = voter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserSkillLevel getUserSkillLevel() {
        return userSkillLevel;
    }

    public void setUserSkillLevel(UserSkillLevel userSkillLevel) {
        this.userSkillLevel = userSkillLevel;
    }
}
