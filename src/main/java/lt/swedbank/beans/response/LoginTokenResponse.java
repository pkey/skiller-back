package lt.swedbank.beans.response;


import lt.swedbank.FakeAuthClasses.FakeTokenHolder;

public class LoginTokenResponse extends Response {

    private String access_token;
    private String id_token;

    public LoginTokenResponse(FakeTokenHolder tokenHolder) {
        this.access_token = tokenHolder.getAccessToken();
        this.id_token = tokenHolder.getIdToken();
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }
}
