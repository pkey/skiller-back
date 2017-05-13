package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;
import lt.swedbank.exceptions.skill.SkillNotFaoundException;

public interface ISkillService {

    Skill addSkill(Long userID, AddSkillRequest skill);
    Skill removeSkill(Long userID, RemoveSkillRequest removeSkillRequest) throws SkillNotFaoundException;
}
