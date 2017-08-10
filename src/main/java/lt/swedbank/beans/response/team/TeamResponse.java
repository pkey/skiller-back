package lt.swedbank.beans.response.team;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.division.DivisionResponse;
import lt.swedbank.beans.response.valueStream.ValueStreamResponse;

import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TeamResponse {
    protected Long id;
    protected String name;

    protected DepartmentResponse department;

    protected DivisionResponse division;

    protected ValueStreamResponse valueStream;

    protected Set<SkillTemplateResponse> skillTemplate;

    public TeamResponse(Team team, Set<SkillTemplateResponse> skillTemplateResponse) {
        this.id = team.getId();
        this.name = team.getName();
        this.department = new DepartmentResponse(team.getDepartment());
        this.division = new DivisionResponse(team.getDepartment().getDivision());
        this.valueStream = team.getValueStream().map(ValueStreamResponse::new).orElse(null);
        this.skillTemplate = skillTemplateResponse;
    }

    public TeamResponse(Team team) {
        this(team, new HashSet<>());
    }

}
