package lt.swedbank.beans.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = "skill")
public class SkillTemplateResponse implements Comparable<SkillTemplateResponse> {

    @NonNull
    private SkillEntityResponse skill;
    @NonNull
    private Integer userCounter;
    @NonNull
    private Double averageLevel;

    @Override
    public int compareTo(SkillTemplateResponse skillTemplateResponse) {
        if (skill.equals(skillTemplateResponse.skill)) {
            return 0;
        }
        return skillTemplateResponse.getUserCounter().compareTo(this.userCounter);
    }

}
