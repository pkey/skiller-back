package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.Skill;

public class TeamSkillTeamplateResponse {

    private SkillEntityResponse skill;

    private Integer counter;

    public TeamSkillTeamplateResponse() {}

    public TeamSkillTeamplateResponse(Skill skill, int counter) {
        this.skill = new SkillEntityResponse(skill);
        this.counter = counter;
    }

    public SkillEntityResponse getSkill() {
        return skill;
    }

    public void setSkill(SkillEntityResponse skill) {
        this.skill = skill;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
