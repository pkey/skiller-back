package lt.swedbank.beans.response.team;

import com.fasterxml.jackson.annotation.JsonInclude;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.division.DivisionResponse;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamResponse {
    protected Long id;
    protected String name;

    protected DepartmentResponse department;

    protected DivisionResponse division;

    protected ValueStreamResponse valueStream;

    protected List<SkillTemplateResponse> skillTemplate;

    public TeamResponse(Team team, List<SkillTemplateResponse> skillTemplateResponse) {
        this.id = team.getId();
        this.name = team.getName();
        this.department = new DepartmentResponse(team.getDepartment());
        this.division = new DivisionResponse(team.getDepartment().getDivision());
        this.valueStream = team.getValueStream().map(ValueStreamResponse::new).orElse(null);
        this.skillTemplate = skillTemplateResponse;
    }

    public TeamResponse(Team team) {
        this(team, new ArrayList<>());
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

    public List<SkillTemplateResponse> getSkillTemplate() {
        return skillTemplate;
    }

    public void setSkillTemplate(List<SkillTemplateResponse> skillTemplate) {
        this.skillTemplate = skillTemplate;
    }
}
