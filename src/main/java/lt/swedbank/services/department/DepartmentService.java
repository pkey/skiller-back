package lt.swedbank.services.department;

import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.department.DepartmentEntityResponse;
import lt.swedbank.beans.response.department.DepartmentOverviewResponse;
import lt.swedbank.beans.response.department.DepartmentResponse;
import lt.swedbank.beans.response.user.UserResponse;
import lt.swedbank.exceptions.department.DepartmentNotFoundException;
import lt.swedbank.repositories.DepartmentRepository;
import lt.swedbank.services.overview.OverviewService;
import lt.swedbank.services.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private OverviewService overviewService;
    @Autowired
    private TeamService teamService;

    public Department getDepartmentById(@NotNull Long id) {
        Department department = departmentRepository.findOne(id);
        if (department == null) {
            throw new DepartmentNotFoundException();
        }
        return department;
    }

    public DepartmentOverviewResponse getDepartmentOverviewByDepartmentId(@NotNull Long id) {
        Department department = this.getDepartmentById(id);

        return new DepartmentOverviewResponse(new DepartmentResponse(department),
                this.getUserResponsesFromDepartment(department),
                this.getDepartmentSkillTemplateResponses(department));

    }

    public Iterable<DepartmentEntityResponse> getAllDepartmentEntityResponseList() {
        return this.getAllDepartments().stream().map(DepartmentEntityResponse::new).collect(Collectors.toList());
    }

    public Set<SkillTemplateResponse> getDepartmentSkillTemplateResponses(Department department) {
        Set<SkillTemplateResponse> skillTemplateResponses = new HashSet<>();
        department.getTeams().forEach(team ->
                skillTemplateResponses.addAll(teamService.getTeamSkillTemplateResponseList(team)));

        return skillTemplateResponses;
    }

    public Set<UserResponse> getUserResponsesFromDepartment(Department department) {
        Set<User> users = new HashSet<>();
        department.getTeams().forEach(team -> users.addAll(team.getUsers()));
        return users.stream().map(UserResponse::new).collect(Collectors.toSet());
    }

    private List<Department> getAllDepartments() {
        return (List<Department>) departmentRepository.findAll();
    }

}
