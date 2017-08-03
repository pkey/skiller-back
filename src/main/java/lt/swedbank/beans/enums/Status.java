package lt.swedbank.beans.enums;

import java.util.Arrays;

public enum Status {
    DISAPPROVED(-1),
    PENDING(0),
    APPROVED(1),
    NEW(2),
    EXPIRED(3);

    private Integer value;

    Status(Integer num) {
        this.value = num;
    }

    @Override
    public String toString() {
        return getStatus(this.value).name();
    }

    public Integer getValue() {
        return value;
    }

    public Status getStatus(Integer value) {
        return Arrays.stream(Status.values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
