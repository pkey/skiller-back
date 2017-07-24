package lt.swedbank.beans.enums;

public enum Status {
    approved ("approved"),
    pending ("pending"),
    disapproved ("disapproved"),
    expired ("expired");

    private final String status;

    private Status(String status) {
        this.status = status;
    }

    public String toString(){
        return this.status;
    }
}
