package lt.swedbank.beans.entity;

import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

@Entity
@Indexed
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"userSkills"})
@ToString(exclude = {"userSkills"})
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @Field
    @NonNull
    private String title;

    @OneToMany(mappedBy = "skill")
    private List<UserSkill> userSkills;
}
