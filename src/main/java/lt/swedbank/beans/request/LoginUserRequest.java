package lt.swedbank.beans.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lt.swedbank.beans.User;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginUserRequest {

    @NotNull(message = "Password is required!")
    @Size(min = 8, max = 45, message = "Password is too short or too long")
    @ApiModelProperty(required = true, example = "123@Abc:)")
    private String password;

    @NotNull(message = "Connection specification is required!")
    @ApiModelProperty(required = true, example = "Username-Password-Authentication")
    private String connection;

    @NotNull(message = "Email is required!")
    @Email(message = "Not an email")
    @ApiModelProperty(required = true, example = "email@email.com")
    private String email;

    public LoginUserRequest() {}

    public LoginUserRequest(User user) {

        setPassword(user.getPassword());
        setConnection(user.getConnection());
        setEmail(user.getEmail());
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
