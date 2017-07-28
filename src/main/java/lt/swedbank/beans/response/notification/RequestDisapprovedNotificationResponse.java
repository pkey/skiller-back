package lt.swedbank.beans.response.notification;

import lt.swedbank.beans.entity.Disapprover;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.response.DisapproverResponse;

import java.util.ArrayList;
import java.util.List;

public class RequestDisapprovedNotificationResponse extends NotificationResponse {

    final private Integer TYPE = 2;

    private List<DisapproverResponse> disapprovers;

    public RequestDisapprovedNotificationResponse(){
    }

    public RequestDisapprovedNotificationResponse(RequestNotification requestNotification) {
        super(requestNotification);
        super.setType(TYPE);
        setDisapproversResponseList(requestNotification.getApprovalRequest().getDisapprovers());
    }

    private void setDisapproversResponseList(List<Disapprover> disapproversList)
    {
        disapprovers = new ArrayList<>();
        for (Disapprover disapprover: disapproversList) {
            disapprovers.add(new DisapproverResponse(disapprover));
        }
    }


    public Integer getType() {
        return TYPE;
    }

    public List<DisapproverResponse> getDisapprovers() {
        return disapprovers;
    }

    public void setDisapprovers(List<DisapproverResponse> disapprovers) {
        this.disapprovers = disapprovers;
    }
}