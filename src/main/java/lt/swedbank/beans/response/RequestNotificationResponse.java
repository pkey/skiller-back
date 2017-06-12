package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.ApprovalRequest;
import lt.swedbank.beans.entity.RequestNotification;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.services.skill.UserSkillLevelService;

public class RequestNotificationResponse {

        private Long id;

        private User receiver;

        private UserEntityResponse sender;

        private SkillEntityResponse skill;


        public RequestNotificationResponse(){}

        public RequestNotificationResponse(Long id, User receiver, ApprovalRequest approvalRequest)
        {
            this.id = id;
            this.receiver = receiver;
        }

        public RequestNotificationResponse(RequestNotification requestNotification)
        {
            this.receiver = requestNotification.getReceiver();
            this.id = requestNotification.getId();
            //TODO fix this shit
            this.skill = new SkillEntityResponse(requestNotification.getApprovalRequest().getUserSkill().getSkill());
            this.sender = new UserEntityResponse(requestNotification.getApprovalRequest().getUserSkill().getUser());

        }

        public User getReceiver() {
            return receiver;
        }

        public void setReceiver(User receiver) {
            this.receiver = receiver;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
}
