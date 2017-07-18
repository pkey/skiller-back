package lt.swedbank.beans.response.notification;

import lt.swedbank.beans.entity.Approver;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.response.ApproverResponse;


import java.util.ArrayList;
import java.util.List;


public class RequestApprovedNotificationResponse extends NotificationResponse {

    final private Integer type = 3;

    private List<ApproverResponse> approvals;

    public RequestApprovedNotificationResponse(){
    }

    public RequestApprovedNotificationResponse(RequestNotification requestNotification) {

        super(requestNotification);
        this.approvals = new ArrayList<>();
        for(Approver approver : requestNotification.getApprovalRequest().getApprovers()) {
            this.approvals.add(new ApproverResponse(approver));
        }
    }


    public Integer getType() {
        return type;
    }

    public List<ApproverResponse> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<ApproverResponse> approvals) {
        this.approvals = approvals;
    }
}
