package lt.swedbank.events.userSkill;

import lombok.Data;
import lombok.NonNull;
import lt.swedbank.beans.entity.UserSkill;

@Data
public class UserSkillAddedEvent {
    @NonNull
    private UserSkill userSkill;
}
