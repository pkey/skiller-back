package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.exceptions.skill.SkillNotFoundException;

public interface ISkillService {

    UserSkill addSkill(Long userID, AddSkillRequest skill);
    UserSkill removeSkill(Long userID, Skill skill) throws SkillNotFoundException;
}
