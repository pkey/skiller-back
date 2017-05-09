package lt.swedbank.beans.response;

import lt.swedbank.beans.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterUserResponse extends Response {

    private String name;

    private String lastName;

    private String email;

    public RegisterUserResponse(User user) {

        name = user.getName();
        lastName = user.getLastName();
        email = user.getEmail();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
