package lt.swedbank.beans.response.team;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.division.DivisionResponse;
import lt.swedbank.beans.response.user.UserEntityResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.beans.response.user.UserWithoutTeamResponse;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeamResponse {
    protected Long id;
    protected String name;

    protected DepartmentResponse department;

    protected DivisionResponse division;

    protected ValueStreamResponse valueStream;

    protected List<UserWithoutTeamResponse> users;

    protected List<TeamSkillTemplateResponse> skillTemplate;

    public TeamResponse() {
    }

    public TeamResponse(Team team, List<TeamSkillTemplateResponse> teamSkillTemplateResponses) {
        this.id = team.getId();
        this.name = team.getName();
        this.department = new DepartmentResponse(team.getDepartment());
        this.division = new DivisionResponse(team.getDepartment().getDivision());
        this.valueStream =  (team.getValueStream() == null) ? null : new ValueStreamResponse(team.getValueStream());
        this.users = team.getUsers() == null ? new ArrayList<>() : team.getUsers().stream().map(UserWithoutTeamResponse::new).collect(Collectors.toList());
        this.skillTemplate = team.getSkillTemplate() == null ? null : teamSkillTemplateResponses;
    }

    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.department = new DepartmentResponse(team.getDepartment());
        this.division = new DivisionResponse(team.getDepartment().getDivision());
        this.valueStream = (team.getValueStream() == null ? null : new ValueStreamResponse(team.getValueStream()));
        this.users = (team.getUsers() == null) ? new ArrayList<>() : team.getUsers().stream().map(UserWithoutTeamResponse::new).collect(Collectors.toList());
        this.skillTemplate = new ArrayList<>();
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

    public List<UserWithoutTeamResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithoutTeamResponse> users) {
        this.users = users;
    }

    public List<TeamSkillTemplateResponse> getSkillTemplate() {
        return skillTemplate;
    }

    public void setSkillTemplate(List<TeamSkillTemplateResponse> skillTemplate) {
        this.skillTemplate = skillTemplate;
    }
}
