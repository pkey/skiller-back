package lt.swedbank.beans.response.notification;

import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.response.ApproverResponse;
import lt.swedbank.beans.response.DisapproverResponse;
import lt.swedbank.beans.response.SkillEntityResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestDisapprovedNotificationResponse extends NotificationResponse {

    final private Integer type = 2;

    private DisapproverResponse disapprover;

    public RequestDisapprovedNotificationResponse(){
    }

    public RequestDisapprovedNotificationResponse(RequestNotification requestNotification) {
        super(requestNotification);
        this.disapprover = new DisapproverResponse(requestNotification.getApprovalRequest().getDisapprover());
    }

    public Integer getType() {
        return type;
    }

    public DisapproverResponse getDisapprover() {
        return disapprover;
    }

    public void setDisapprover(DisapproverResponse disapprover) {
        this.disapprover = disapprover;
    }
}