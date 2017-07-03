package lt.swedbank.beans.entity;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

@Entity
@Indexed
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @Field
    private String title;

    @ContainedIn
    @OneToMany(mappedBy = "skill")
    private List<UserSkill> userSkills;

    public Skill() {
    }

    public Skill(String title) {
        this.title = title;
        capitalizeTitle();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserSkill> getUserSkills() {
        return userSkills;
    }

    public void setUserSkills(List<UserSkill> userSkills) {
        this.userSkills = userSkills;
    }

    private void capitalizeTitle() {
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
    }
}
