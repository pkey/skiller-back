package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;

public class RequestNotificationResponse {

        private Long id;

        private User receiver;

        private ApprovalRequest approvalRequest;

        public RequestNotificationResponse(){}

        public RequestNotificationResponse(Long id, User receiver, ApprovalRequest approvalRequest)
        {
            this.approvalRequest = approvalRequest;
            this.id = id;
            this.receiver = receiver;
        }

        public RequestNotificationResponse(RequestNotification requestNotification)
        {
            this.receiver = requestNotification.getReceiver();
            this.id = requestNotification.getId();
            this.approvalRequest = requestNotification.getApprovalRequest();
        }

        public User getReceiver() {
            return receiver;
        }

        public void setReceiver(User receiver) {
            this.receiver = receiver;
        }

        public ApprovalRequest getApprovalRequest() {
            return approvalRequest;
        }

        public void setApprovalRequest(ApprovalRequest approvalRequest) {
            this.approvalRequest = approvalRequest;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
}
