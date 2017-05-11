package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.RemoveSkillRequest;

public interface IUserService {
    User getUserByEmail(String email);
    Skill removeUserSkill(String email, RemoveSkillRequest removeSkillRequest);
}
