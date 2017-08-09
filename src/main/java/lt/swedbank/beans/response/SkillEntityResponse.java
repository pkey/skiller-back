package lt.swedbank.beans.response;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lt.swedbank.beans.entity.Skill;

import javax.validation.constraints.NotNull;

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
