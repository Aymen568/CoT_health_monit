package tn.cot.healthmonitoring.security;



import jakarta.nosql.Column;
import jakarta.nosql.Entity;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class AccessToken {
    @Column
    private String token;
    @Column
    private String jwtSecret;
    @Column
    private LocalDateTime expired;

    @Deprecated
    public AccessToken() {
    }

    public AccessToken(String token, String jwtSecret, Duration duration) {
        this.token = token;
        this.jwtSecret = jwtSecret;
        this.expired = LocalDateTime.now().plus(duration);
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpired() {
        return expired;
    }
    public String getJwtSecret() {
        return jwtSecret;
    }

    public LocalDateTime getExpiration() {
        return expired;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                ", jwtSecret='" + jwtSecret + '\'' +
                ", expiration=" + expired +
                '}';
    }
}