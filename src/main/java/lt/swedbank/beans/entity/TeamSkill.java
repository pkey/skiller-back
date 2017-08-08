package lt.swedbank.beans.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TeamSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @NonNull
    private Team team;
    @ManyToOne
    @NonNull
    private Skill skill;
    @NonNull
    private Integer skillCount;
    @NonNull
    private Double skillLevelAverage;
    @CreationTimestamp
    private LocalDateTime creationTime;
}
