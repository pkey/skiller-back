package lt.swedbank.beans.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lt.swedbank.beans.request.RegisterUserRequest;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
public class User implements Comparable<User> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String lastName;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String connection;
    private String email;
    @JsonIgnore
    private String authId;

    @OneToMany
    @JoinColumn(name = "userid")
    private List<UserSkill> userSkills = new LinkedList<>();

    @ManyToOne
    private Team team;


    public User() {}

    public User(RegisterUserRequest registerUserRequest) {

        setName(registerUserRequest.getName());
        setLastName(registerUserRequest.getLastName());
        setConnection(registerUserRequest.getConnection());
        setEmail(registerUserRequest.getEmail());
        setPassword(registerUserRequest.getPassword());
    }

    @Override
    public int compareTo(User user) {
        return this.getFullName().compareTo(user.getFullName());
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

    private String getFullName()
    {
        return this.name + " " + this.name;
    }

    public List<UserSkill> getUserSkills() {
        return userSkills;
    }

    public void setUserSkills(List<UserSkill> userSkills) {
        this.userSkills = userSkills;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
