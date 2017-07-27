package lt.swedbank.beans.response.team;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.division.DivisionResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamResponse {
    protected Long id;
    protected String name;

    protected DepartmentResponse department;

    protected DivisionResponse division;

    protected ValueStreamResponse valueStream;

    protected List<UserWithSkillsResponse> users;

    protected List<TeamSkillTemplateResponse> skillTemplate;

    public TeamResponse(Team team, List<UserWithSkillsResponse> usersWithSkills, List<TeamSkillTemplateResponse> teamSkillTemplateResponses) {
        this.id = team.getId();
        this.name = team.getName();
        this.department = new DepartmentResponse(team.getDepartment());
        this.division = new DivisionResponse(team.getDepartment().getDivision());
        this.valueStream = team.getValueStream() == null ? null : new ValueStreamResponse(team.getValueStream());
        this.users =  usersWithSkills;
        this.skillTemplate =  teamSkillTemplateResponses;
    }

    public TeamResponse(Team team, List<UserWithSkillsResponse> usersWithSkills) {
        this(team, usersWithSkills, new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepartmentResponse getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponse department) {
        this.department = department;
    }

    public DivisionResponse getDivision() {
        return division;
    }

    public void setDivision(DivisionResponse division) {
        this.division = division;
    }

    public ValueStreamResponse getValueStream() {
        return valueStream;
    }

    public void setValueStream(ValueStreamResponse valueStream) {
        this.valueStream = valueStream;
    }

    public List<UserWithSkillsResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithSkillsResponse> users) {
        this.users = users;
    }

    public List<TeamSkillTemplateResponse> getSkillTemplate() {
        return skillTemplate;
    }

    public void setSkillTemplate(List<TeamSkillTemplateResponse> skillTemplate) {
        this.skillTemplate = skillTemplate;
    }
}
