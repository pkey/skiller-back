package lt.swedbank.beans.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class NotificationAnswerRequest {

    @NotNull(message = "Notification ID is required!")
    @ApiModelProperty(required = true, example = "12")
    private Long notificationId;

    @NotNull(message = "Message is required!")
    @ApiModelProperty(required = true, example = "Real expert here. Approved!!")
    private String message;

    @NotNull(message = "Required string!")
    @ApiModelProperty(required = true, example = "-1")
    private Integer approved;

    public NotificationAnswerRequest() {}

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
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
