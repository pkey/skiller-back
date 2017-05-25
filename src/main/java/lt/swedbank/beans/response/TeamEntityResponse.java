package lt.swedbank.beans.response;


import lt.swedbank.beans.entity.Team;

public class TeamEntityResponse {
    private Long id;
    private String name;

    public TeamEntityResponse(Team team) {
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
