package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.Department;
import lt.swedbank.beans.entity.Team;

public class DepartmentEntityResponse extends Response {

    private Long id;
    private String name;

    private Iterable<Team> teams;

    public DepartmentEntityResponse(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.teams = department.getTeams();
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

    public Iterable<Team> getTeams() {
        return teams;
    }

    public void setTeams(Iterable<Team> teams) {
        this.teams = teams;
    }

}