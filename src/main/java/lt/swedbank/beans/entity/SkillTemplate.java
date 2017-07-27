package lt.swedbank.beans.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class SkillTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name="team_id", unique=true)
    private Team team;

    @ManyToMany
    private List<Skill> skills;

    public SkillTemplate() {}

    public SkillTemplate(Team team, List<Skill> skills) {
        this.team = team;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
