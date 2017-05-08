package lt.swedbank.beans.response;

import lt.swedbank.beans.User;

public class GetUserResponse extends Response{

    private String name;

    private String lastName;

    private String email;

    public GetUserResponse(User user) {

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
