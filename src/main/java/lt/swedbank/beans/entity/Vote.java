package lt.swedbank.beans.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(
        name = "vote",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"voter_id", "user_skill_level_id" })
        }
)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NonNull
    private User voter;

    @ManyToOne
    @NonNull
    private UserSkillLevel userSkillLevel;

    @Size(max=500)
    @NonNull
    private String message;


}
