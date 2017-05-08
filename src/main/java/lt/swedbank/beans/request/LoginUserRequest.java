package lt.swedbank.beans.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginUserRequest {

    @NotNull(message = "Password is required!")
    @Size(min = 8, max = 45, message = "Password is too short or too long")
    private String password;

    @NotNull(message = "Connection specification is required!")
    private String connection;

    @NotNull(message = "Email is required!")
    @Email(message = "Not an email")
    private String email;

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
