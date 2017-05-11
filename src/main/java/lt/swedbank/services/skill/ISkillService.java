package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.RemoveSkillRequest;

public interface ISkillService {

    Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest);
}
