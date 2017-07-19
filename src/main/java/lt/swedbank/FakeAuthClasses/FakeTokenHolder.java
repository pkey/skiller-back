package lt.swedbank.FakeAuthClasses;

import com.auth0.json.auth.TokenHolder;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FakeTokenHolder {

    @JsonProperty("access_token")
    private String accessToken;

    private String id_token;

    public String getIdToken() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public FakeTokenHolder(String token){
        accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setToken(String token) {
        accessToken = token;
    }


}
