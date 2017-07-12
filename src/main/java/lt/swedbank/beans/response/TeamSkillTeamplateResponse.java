package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.Skill;

public class TeamSkillTeamplateResponse {

    private SkillEntityResponse skill;

    private Integer userCounter;

    private Double averageLevel;

    public TeamSkillTeamplateResponse() {}

    public TeamSkillTeamplateResponse(Skill skill, int userCounter, double averageLevel) {
        this.skill = new SkillEntityResponse(skill);
        this.userCounter = userCounter;
        this.averageLevel = averageLevel;
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

    public Double getAverageLevel() {
        return averageLevel;
    }

    public void setAverageLevel(Double averageLevel) {
        this.averageLevel = averageLevel;
    }
}
