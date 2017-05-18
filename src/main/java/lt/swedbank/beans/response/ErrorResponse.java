package lt.swedbank.beans.response;


import lt.swedbank.exceptions.ApplicationException;

public class ErrorResponse extends Response {

    public String message;

    public ErrorResponse(ApplicationException ex) {
        this.message = ex.getErrorCause();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
