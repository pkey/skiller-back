package lt.swedbank.beans.response;

import lt.swedbank.beans.entity.Skill;

import java.util.Comparator;

public class TeamSkillTemplateResponse implements Comparator<TeamSkillTemplateResponse>, Comparable<TeamSkillTemplateResponse>{

    private SkillEntityResponse skill;

    private Integer userCounter;

    private Double averageLevel = 0.0;

    public TeamSkillTemplateResponse() {}

    public TeamSkillTemplateResponse(Skill skill, int userCounter, double averageLevel) {
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

    @Override
    public int compareTo(TeamSkillTemplateResponse teamSkillTemplateResponse) {
        return this.userCounter.compareTo(teamSkillTemplateResponse.getUserCounter());
    }

    @Override
    public int compare(TeamSkillTemplateResponse teamSkillTemplateResponse, TeamSkillTemplateResponse t1) {
        return teamSkillTemplateResponse.getUserCounter().compareTo(t1.getUserCounter());
    }
}
