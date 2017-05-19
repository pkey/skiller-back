package lt.swedbank.beans.entity.unit;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;

    protected String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private Set<Team> teams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Team> getTeams() {
        return teams;
    }


    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
