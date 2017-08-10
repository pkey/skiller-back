package lt.swedbank.beans.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"team"})
@ToString(exclude = {"team"})
public class SkillTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="team_id", unique=true)
    @NonNull
    private Team team;

    @ManyToMany
    @NonNull
    private List<Skill> skills;

    public void addSkill(@NotNull Skill skill) {
        if (skills == null) {
            skills = new ArrayList<>();
        }
        skills.add(skill);
    }

}
