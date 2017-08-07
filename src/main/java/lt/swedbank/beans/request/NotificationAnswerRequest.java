package lt.swedbank.beans.request;

import io.swagger.annotations.ApiModelProperty;
import lt.swedbank.beans.enums.Status;

import javax.validation.constraints.NotNull;

public class NotificationAnswerRequest {

    @NotNull(message = "Notification ID is required!")
    @ApiModelProperty(required = true, example = "12")
    private Long notificationId;

    @NotNull(message = "Message is required!")
    @ApiModelProperty(required = true, example = "Real expert here. Approved!!")
    private String message;

    private Status status;

    public NotificationAnswerRequest() {}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status approved) {
        this.status = approved;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
