package lt.swedbank.exceptions;

public class ApplicationException extends RuntimeException {
    protected String errorCause;

    public ApplicationException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
