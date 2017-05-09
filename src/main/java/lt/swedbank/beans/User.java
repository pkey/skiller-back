package lt.swedbank.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lt.swedbank.beans.request.RegisterUserRequest;

import javax.persistence.*;


@Entity
public class User {

    @Id
    @JsonIgnore
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

    public User() {}

    public User(RegisterUserRequest registerUserRequest) {

        setName(registerUserRequest.getName());
        setLastName(registerUserRequest.getLastName());
        setConnection(registerUserRequest.getConnection());
        setEmail(registerUserRequest.getEmail());
        setPassword(registerUserRequest.getPassword());
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
}
