package lt.swedbank.beans.entity;

import lombok.*;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Indexed
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"userSkillLevels"})
@ToString(exclude = {"userSkillLevels"})
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @NonNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Skill skill;

    @OneToMany(mappedBy = "userSkill", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserSkillLevel> userSkillLevels;

    public String getTitle() {
        return skill.getTitle();
    }


    public void addUserSkillLevel(UserSkillLevel userSkillLevel) {
        if (userSkillLevels == null || userSkillLevels.isEmpty())
            userSkillLevels = new ArrayList<>();
        this.userSkillLevels.add(userSkillLevel);
    }
}
