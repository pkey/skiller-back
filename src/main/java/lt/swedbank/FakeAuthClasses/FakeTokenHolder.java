package lt.swedbank.FakeAuthClasses;

import com.auth0.json.auth.TokenHolder;
import com.fasterxml.jackson.annotation.JsonProperty;

class FakeTokenHolder extends TokenHolder{

    @JsonProperty("access_token")
    private String accessToken;

    public FakeTokenHolder(String token){
        accessToken = token;
    }

    public String getToken() {
        return accessToken;
    }

    public void setToken(String token) {
        accessToken = token;
    }
}
