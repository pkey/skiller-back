package lt.swedbank.beans.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lt.swedbank.beans.request.RegisterUserRequest;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;


@Entity
@Indexed
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field
    private String name;

    @Field
    private String lastName;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String connection;

    private String email;

    private String authId;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<UserSkill> userSkills;

    @ManyToOne
    private Team team;


    public User() {
    }

    public User(RegisterUserRequest registerUserRequest) {

        setName(registerUserRequest.getName());
        setLastName(registerUserRequest.getLastName());
        setConnection(registerUserRequest.getConnection());
        setEmail(registerUserRequest.getEmail());
        setPassword(registerUserRequest.getPassword());
        capitalizeUserNameAndLastName();

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public List<UserSkill> getUserSkills() {
        return userSkills;
    }

    public void setUserSkills(List<UserSkill> userSkills) {
        this.userSkills = userSkills;
    }

    public void setUserSkill(UserSkill userSkill) {
        this.userSkills.add(userSkill);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    private void capitalizeUserNameAndLastName() {
        this.setName(name.substring(0, 1).toUpperCase() + name.substring(1));
        this.setLastName(lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
    }

}
