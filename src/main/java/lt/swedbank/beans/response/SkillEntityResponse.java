package lt.swedbank.beans.response;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.response.serializers.UserSkillsSerializer;

import java.util.List;

public class SkillEntityResponse extends Response {

    private String title;
    private Long id;

    @JsonSerialize(using = UserSkillsSerializer.class)
    private List<UserSkill> userSkills;

    public SkillEntityResponse(Skill skill)
    {
        this.id = skill.getId();
        this.title = skill.getTitle();
    }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
}
