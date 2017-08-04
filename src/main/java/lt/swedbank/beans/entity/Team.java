package lt.swedbank.beans.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @OneToMany(mappedBy = "team")
    private List<User> users;

    @ManyToOne
    @NonNull
    private Department department;

    @ManyToOne
    private ValueStream valueStream;

    @OneToOne(mappedBy = "team", cascade = CascadeType.PERSIST)
    private SkillTemplate skillTemplate;

    public Optional<ValueStream> getValueStream() {
        return Optional.ofNullable(valueStream);
    }

    public void setUsers(List<User> users) {
        this.users = users;
        for (User user : users) {
            user.setTeam(this);
        }
    }

    public void addUser(User user) {
        if (users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
    }

}
