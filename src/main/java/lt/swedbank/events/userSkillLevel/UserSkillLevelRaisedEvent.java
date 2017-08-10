package lt.swedbank.events.userSkillLevel;

import lombok.Data;
import lombok.NonNull;
import lt.swedbank.beans.entity.UserSkillLevel;

@Data
public class UserSkillLevelRaisedEvent {
    @NonNull
    UserSkillLevel userSkillLevel;
}
