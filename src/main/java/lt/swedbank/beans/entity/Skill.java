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


    public Skill(String title) {
        this.title = title;
        //TODO where this thing should be?
        capitalizeTitle();
    }


    private void capitalizeTitle() {
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
    }
}
