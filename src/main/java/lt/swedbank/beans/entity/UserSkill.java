package lt.swedbank.beans.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Indexed
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Skill skill;

    @OneToMany
    private Set<SkillLevel> skillLevel;

    private String description;

    @CreationTimestamp
    private Date updated;

    public UserSkill(){}

    public UserSkill(User user, Skill skill) {
        this.skill = skill;
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

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getTitle()
    {
        return skill.getTitle();
    }

    public Set<SkillLevel> getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Set<SkillLevel> skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
