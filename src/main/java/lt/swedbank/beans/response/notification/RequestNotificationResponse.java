package lt.swedbank.beans.response.notification;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.response.SkillEntityResponse;

public class RequestNotificationResponse extends NotificationResponse {

    final private Integer TYPE = 1;

    public RequestNotificationResponse(){
    }

    public RequestNotificationResponse(RequestNotification requestNotification) {
        super(requestNotification);
        super.setType(TYPE);
    }
    
    public Integer getType() {
        return TYPE;
    }


}
