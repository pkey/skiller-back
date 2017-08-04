package lt.swedbank.beans.response;


import com.sun.istack.internal.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lt.swedbank.beans.entity.Skill;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SkillEntityResponse extends Response {

    @NonNull
    private Long id;
    @NonNull
    private String title;

    public SkillEntityResponse(@NotNull Skill skill) {
        id = skill.getId();
        title = skill.getTitle();
    }

}
