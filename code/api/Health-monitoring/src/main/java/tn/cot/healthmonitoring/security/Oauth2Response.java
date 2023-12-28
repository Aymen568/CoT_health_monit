package tn.cot.healthmonitoring.security;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbVisibility;
import tn.cot.healthmonitoring.utils.FieldPropertyVisibilityStrategy;


@JsonbVisibility(FieldPropertyVisibilityStrategy.class)
public class Oauth2Response {

    @JsonbProperty("access_token")
    private String accessToken;

    @JsonbProperty("expiresIn")
    private int expiresIn;

    @JsonbProperty("username")
    private String userId;

    @JsonbProperty("refresh_token")
    private String refreshToken;


    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserId() {return userId;}

    static Oauth2Response of(AccessToken accessToken, RefreshToken refreshToken, int expiresIn, String userId) {
        Oauth2Response response = new Oauth2Response();
        response.accessToken = accessToken.getToken();
        response.refreshToken = refreshToken.getToken();
        response.expiresIn = expiresIn;
        response.userId = userId;
        return response;
    }

}