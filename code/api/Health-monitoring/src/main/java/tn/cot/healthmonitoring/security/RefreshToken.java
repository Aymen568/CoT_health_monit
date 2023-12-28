package tn.cot.healthmonitoring.security;



import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.security.enterprise.identitystore.openid.AccessToken;

import java.util.Objects;

@Entity
public class RefreshToken{

    @Column
    private String token;

    @Column
    private AccessToken accessToken;

    public RefreshToken() {

    }
    RefreshToken(Token token, AccessToken accessToken) {
        this.token = token.get();
        this.accessToken = accessToken;
    }

    @Deprecated
    RefreshToken(Token generate, tn.cot.healthmonitoring.security.AccessToken accessToken) {
    }

    public String getToken() {
        return token;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "token='" + token + '\'' +
                ", accessToken=" + accessToken +
                '}';
    }
}