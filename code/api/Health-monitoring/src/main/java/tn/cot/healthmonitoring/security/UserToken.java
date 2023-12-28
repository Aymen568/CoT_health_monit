package tn.cot.healthmonitoring.security;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import tn.cot.healthmonitoring.repositories.UserTokenRepository;
import jakarta.security.enterprise.identitystore.openid.AccessToken;

import java.util.*;

@Entity
public class UserToken {
    @Id
    private String email;
    @Column
    private Set<RefreshToken> tokens;
    UserToken(String email) {
        this.email = email;
    }
    UserToken(String email, Set<RefreshToken> tokens) {
        this.email = email;
        this.tokens = tokens;
    }
    @Deprecated
    public UserToken() {
    }

    public String getEmail() {
        return email;
    }

    public Set<RefreshToken> getTokens() {
        if (tokens == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(tokens);
    }
    void add(RefreshToken refreshToken) {
        initiateTokens();
        this.tokens.add(refreshToken);
    }

    RefreshToken update(AccessToken accessToken, String refreshTokenText, UserTokenRepository repository) {
        initiateTokens();
        this.tokens.removeIf(r -> refreshTokenText.equals(r.getToken()));
        RefreshToken refreshToken = new RefreshToken(Token.generate(), accessToken);
        this.tokens.add(refreshToken);
        repository.save(this);
        return refreshToken;
    }

    void remove(String token) {
        initiateTokens();
        this.tokens.removeIf(r -> token.equals(r.getToken()));
    }

    public Optional<AccessToken> findAccessToken(String accessToken) {
        initiateTokens();
        return this.tokens.stream().map(RefreshToken::getAccessToken)
                .filter(a -> a.getToken().equals(accessToken))
                .findFirst();
    }

    private void initiateTokens() {
        if (this.tokens == null) {
            this.tokens = new HashSet<>();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserToken userToken = (UserToken) o;
        return Objects.equals(email, userToken.email) && Objects.equals(tokens, userToken.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, tokens);
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "email='" + email + '\'' +
                ", tokens=" + tokens +
                '}';
    }
}