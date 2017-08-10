package lt.swedbank.beans.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"departments"})
@ToString(exclude = {"departments"})
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NonNull
    protected String name;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    private List<Department> departments;

}
