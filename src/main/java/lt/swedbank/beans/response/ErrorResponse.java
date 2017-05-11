package lt.swedbank.beans.response;


public class ErrorResponse extends Response {

    public String message;

    public ErrorResponse(Exception ex) {
        this.message = ex.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
