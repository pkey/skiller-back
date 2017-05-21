package lt.swedbank.beans.response;



public class ErrorResponse extends Response {

    private String message;

    public ErrorResponse(RuntimeException ex) {
        this.message = ex.getMessage();
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
