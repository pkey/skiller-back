package lt.swedbank.exceptions;


import org.springframework.http.HttpStatus;

public class MainException extends RuntimeException {

    protected String messageCode;
    protected HttpStatus statusCode;


    public MainException() {
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
