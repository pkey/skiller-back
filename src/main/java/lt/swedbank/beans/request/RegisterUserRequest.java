package lt.swedbank.beans.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lt.swedbank.beans.User;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterUserRequest {

    @NotNull(message = "Name is required!")
    private String name;

    @NotNull(message = "Last name is required!")
    private String lastName;

    @NotNull(message = "Password is required!")
    @Size(min = 8, max = 45, message = "Password is too short or too long.")
    private String password;

    @NotNull(message = "Connection is required!")
    private String connection;

    @NotNull(message = "Email is required!")
    @Email(message = "Not an email")
    private String email;

    public RegisterUserRequest() {}

    public RegisterUserRequest(User user) {

        setName(user.getName());
        setLastName(user.getLastName());
        setPassword(user.getPassword());
        setConnection(user.getConnection());
        setEmail(user.getEmail());
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
