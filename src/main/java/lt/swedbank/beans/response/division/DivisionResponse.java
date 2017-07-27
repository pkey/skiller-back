package lt.swedbank.beans.response.division;


import lt.swedbank.beans.entity.Division;

import javax.validation.constraints.NotNull;

public class DivisionResponse {

    private Long id;
    private String name;

    public DivisionResponse() {
    }

    public DivisionResponse(@NotNull Division division) {
        this.id = division.getId();
        this.name = division.getName();
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
