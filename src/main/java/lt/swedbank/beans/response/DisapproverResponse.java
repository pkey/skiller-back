package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.Approver;
import lt.swedbank.beans.entity.Disapprover;

public class DisapproverResponse {

    private String name;
    private String message;

    public DisapproverResponse() {}

    public DisapproverResponse(Disapprover disapprover) {
        this.name = disapprover.getUser().getName();
        this.message = disapprover.getMessage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
