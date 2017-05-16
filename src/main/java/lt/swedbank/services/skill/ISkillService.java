package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;

public interface ISkillService {

    UserSkill addSkill(Long userID, AddSkillRequest skill) throws SkillAlreadyExistsException;
    UserSkill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest) throws SkillNotFoundException;
}
