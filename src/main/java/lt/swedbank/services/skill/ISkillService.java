package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;

public interface ISkillService {

    Skill addSkill(Long userID, AddSkillRequest skill);
}
