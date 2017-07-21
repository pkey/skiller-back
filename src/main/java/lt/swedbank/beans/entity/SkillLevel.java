package lt.swedbank.beans.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SkillLevel implements  Comparable<SkillLevel> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long level;

    private String title;

    private String description;


    public SkillLevel() {
    }

    public SkillLevel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(SkillLevel o) {
        return this.getLevel().compareTo(o.getLevel());
    }


}
