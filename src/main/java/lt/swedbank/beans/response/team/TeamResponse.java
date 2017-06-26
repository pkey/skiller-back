package lt.swedbank.beans.response.team;


import lt.swedbank.beans.entity.Team;

public class TeamResponse {
    protected Long id;
    protected String name;

    public TeamResponse() {
    }

    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
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
}
