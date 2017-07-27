package lt.swedbank.beans.response.team;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.division.DivisionResponse;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;

import java.util.ArrayList;
import java.util.List;

public class TeamResponse {

    protected Long id;

    protected String name;

    protected DepartmentResponse department;

    protected DivisionResponse division;

    protected ValueStreamResponse valueStream;

    protected List<TeamSkillTemplateResponse> skillTemplate;

    public TeamResponse(Team team, List<TeamSkillTemplateResponse> teamSkillTemplateResponses) {
        this.id = team.getId();
        this.name = team.getName();
        this.department = new DepartmentResponse(team.getDepartment());
        this.division = new DivisionResponse(team.getDepartment().getDivision());
        this.valueStream =  new ValueStreamResponse(team.getValueStream());
        this.skillTemplate =  teamSkillTemplateResponses;
    }

    public TeamResponse(Team team) {
        this(team, new ArrayList<>());
    }
}
