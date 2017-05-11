package lt.swedbank.services.user;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.request.AddSkillRequest;
import lt.swedbank.beans.request.RemoveSkillRequest;

public interface IUserService {
    User getUserByEmail(String email);
    Skill addUserSkill(String email, AddSkillRequest addSkillRequest);
    Skill removeUserSkill(String email, RemoveSkillRequest removeSkillRequest);
}
