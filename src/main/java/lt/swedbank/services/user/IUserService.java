package lt.swedbank.services.user;

import lt.swedbank.beans.Skill;
import lt.swedbank.beans.User;
import lt.swedbank.beans.request.AddSkillRequest;

public interface IUserService {
    User getUserByEmail(String email);
    Skill addUserSkill(String email, AddSkillRequest addSkillRequest);
}
