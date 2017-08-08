package lt.swedbank.beans.response.user;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.Response;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.userSkill.UserSkillResponse;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResponse extends Response{

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private List<? extends UserSkillResponse> skills;
    private TeamResponse team;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.team = user.getTeam().map(TeamResponse::new).orElse(null);
    }

}
