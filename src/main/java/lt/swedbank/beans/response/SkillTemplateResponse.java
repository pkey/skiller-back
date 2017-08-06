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
        return this.userCounter.compareTo(skillTemplateResponse.getUserCounter());
    }

}
