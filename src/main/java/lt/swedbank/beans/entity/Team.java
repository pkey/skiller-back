package lt.swedbank.beans.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<User> users;

    @ManyToOne
    private Department department;

    @ManyToOne
    private ValueStream valueStream;

    @OneToOne(mappedBy = "team")
    private SkillTemplate skillTemplate;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        for (User user : users) {
            user.setTeam(this);
        }
    }

    public ValueStream getValueStream() {
        return valueStream;
    }

    public void setValueStream(ValueStream valueStream) {
        this.valueStream = valueStream;
    }

    public SkillTemplate getSkillTemplate() {
        return skillTemplate;
    }

    public void setSkillTemplate(SkillTemplate skillTemplate) {
        this.skillTemplate = skillTemplate;
    }
}
