package lt.swedbank.beans.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lt.swedbank.beans.request.RegisterUserRequest;
import org.hibernate.annotations.Cascade;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Entity
@Indexed
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field
    @NonNull
    private String name;

    @Field
    @NonNull
    private String lastName;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String connection;

    @NonNull
    private String email;

    private String authId;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UserSkill> userSkills;

    @ManyToOne(cascade = CascadeType.ALL)
    private Team team;

    public User(RegisterUserRequest registerUserRequest) {
        setName(registerUserRequest.getName());
        setLastName(registerUserRequest.getLastName());
        setConnection(registerUserRequest.getConnection());
        setEmail(registerUserRequest.getEmail());
        setPassword(registerUserRequest.getPassword());
    }

    public void addUserSkill(UserSkill userSkill) {
        if (userSkills == null) {
            this.userSkills = new ArrayList<>();
        }
        this.userSkills.add(userSkill);
    }

    public Optional<Team> getTeam() {
        return Optional.ofNullable(team);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Department getDepartment() {
        if (team != null) {
            return team.getDepartment();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return getName() + " " + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
