package lt.swedbank.beans.response.notification;

import lt.swedbank.beans.entity.Approver;

public class ApproverResponse {

    private String name;
    private String message;

    public ApproverResponse() {}

    public ApproverResponse(Approver approver) {
        this.name = approver.getUser().getName();
        this.message = approver.getMessage();
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
