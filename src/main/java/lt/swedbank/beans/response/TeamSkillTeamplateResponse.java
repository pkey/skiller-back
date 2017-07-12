package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.Skill;

public class TeamSkillTeamplateResponse {

    private SkillEntityResponse skill;

    private Integer userCounter;

    public TeamSkillTeamplateResponse() {}

    public TeamSkillTeamplateResponse(Skill skill, int userCounter) {
        this.skill = new SkillEntityResponse(skill);
        this.userCounter = userCounter;
    }

    public SkillEntityResponse getSkill() {
        return skill;
    }

    public void setSkill(SkillEntityResponse skill) {
        this.skill = skill;
    }

    public Integer getUserCounter() {
        return userCounter;
    }

    public void setUserCounter(Integer userCounter) {
        this.userCounter = userCounter;
    }
}
