package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.Disapprover;

public class DisapproverResponse {

    private String name;
    private String lastName;
    private String message;

    public DisapproverResponse() {}

    public DisapproverResponse(Disapprover disapprover) {
        this.name = disapprover.getUser().getName();
        this.lastName = disapprover.getUser().getLastName();
        this.message = disapprover.getMessage();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
