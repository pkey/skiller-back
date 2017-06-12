package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.*;
import lt.swedbank.services.skill.UserSkillLevelService;

public class RequestNotificationResponse {

    private Long id;

    private String message;

    private UserEntityResponse sender;

    private SkillEntityResponse skill;

    public RequestNotificationResponse(Long id, User sender, Skill skill, String message)
    {
        this.id = id;
        this.skill = new SkillEntityResponse(skill);
        this.sender =  new UserEntityResponse(sender);
        this.message = message;
    }

    public UserEntityResponse getSender() {
        return sender;
    }

    public void setSender(UserEntityResponse sender) {
        this.sender = sender;
    }

    public SkillEntityResponse getSkill() {
        return skill;
    }
    public void setMessage(String message) {
        this.message = message;
    }


    public void setSkill(SkillEntityResponse skill) {
        this.skill = skill;
    }

    public RequestNotificationResponse(){}



    public RequestNotificationResponse(RequestNotification requestNotification)
    {
        this.id = requestNotification.getId();
        //TODO fix this shit
        this.skill = new SkillEntityResponse(requestNotification.getApprovalRequest().getUserSkill().getSkill());
        this.sender = new UserEntityResponse(requestNotification.getApprovalRequest().getUserSkill().getUser());
        this.message = requestNotification.getApprovalRequest().getMessage();

    }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
}
