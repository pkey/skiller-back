package lt.swedbank.beans.response;

import lombok.Data;
import lombok.NonNull;

import java.util.Comparator;

@Data
public class SkillTemplateResponse implements Comparator<SkillTemplateResponse>, Comparable<SkillTemplateResponse> {

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

    @Override
    public int compare(SkillTemplateResponse skillTemplateResponse, SkillTemplateResponse t1) {
        return skillTemplateResponse.getUserCounter().compareTo(t1.getUserCounter());
    }
}
