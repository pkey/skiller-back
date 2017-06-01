package lt.swedbank.beans.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SkillLevelDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long level;

    private String description;

    public SkillLevelDescription() {}

    public SkillLevelDescription(String description)
    {
        this.description = description;
    }

    public Long getId() {
        return level;
    }

    public void setId(Long level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
