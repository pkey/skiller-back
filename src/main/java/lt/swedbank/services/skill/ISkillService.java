package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;

public interface ISkillService {

    Skill addSkill(Long userID, AddSkillRequest skill) throws SkillAlreadyExistsException;

    Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest);
}
