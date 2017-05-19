package lt.swedbank.exceptions;

public enum ExceptionMessage {
    USER_NOT_FOUND ("user_not_found"),
    SKILL_ALREADY_EXISTS ("skill_already_exists"),
    SKILL_NOT_FOUND ("skill_not_found");

    private String msg;

    ExceptionMessage(String msg){
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }
}
