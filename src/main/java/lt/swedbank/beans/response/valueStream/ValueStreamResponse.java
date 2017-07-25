package lt.swedbank.beans.response.valueStream;

import lt.swedbank.beans.entity.ValueStream;

public class ValueStreamResponse {

    private Long id;
    private String name;

    public ValueStreamResponse() {
    }

    public ValueStreamResponse(ValueStream valueStream) {
        id = valueStream.getId();
        name = valueStream.getName();
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
