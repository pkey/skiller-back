package lt.swedbank.beans.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class NotificationAnswerRequest {

    @NotNull(message = "Notification ID is required!")
    @ApiModelProperty(required = true, example = "12")
    private Long notificationId;

    @NotNull(message = "Approver ID is required!")
    @ApiModelProperty(required = true, example = "18")
    private Long approverId;

    @NotNull(message = "Message is required!")
    @ApiModelProperty(required = true, example = "Real expert here. Approved!!")
    private String message;

    @NotNull(message = "Boolean is required!")
    @ApiModelProperty(required = true, example = "true")
    private Boolean isApproved;

    public NotificationAnswerRequest() {}


    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
