package lt.swedbank.exceptions;


public class MainException extends RuntimeException {

    protected String messageCode;

    public MainException() {
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }
}
