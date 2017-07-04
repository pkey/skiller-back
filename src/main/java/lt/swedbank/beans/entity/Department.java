package lt.swedbank.beans.entity;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

@Entity
@Indexed
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Field
    protected String name;

    @ContainedIn
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Team> teams;

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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
