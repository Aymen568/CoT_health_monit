package tn.cot.healthmonitoring.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import tn.cot.healthmonitoring.exceptions.UserNotAuthorizedException;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.repositories.UserTokenRepository;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class Oauth2Service {

    private static final Config config = ConfigProvider.getConfig();

    static final int EXPIRE_IN = config.getValue("jwt.lifetime.duration", Integer.class);
    static final Duration EXPIRES = Duration.ofSeconds(EXPIRE_IN);

    private UserTokenRepository userTokenRepository;

    @Inject
    private SecurityService securityService;

    @Inject
    private jakarta.validation.Validator validator;

    public Oauth2Response token(Oauth2Request request) {
        final User user = securityService.findBy(request.getEmail(), request.getPassword());
        System.out.println(user.toString());
        Optional<UserToken> optionalUserToken = userTokenRepository.findByEmail(request.getEmail());
        UserToken userToken;
        if (optionalUserToken.isPresent()) {
            userToken = optionalUserToken.get();
        } else {
            userToken = new UserToken(request.getEmail());
        }

        final Token token = Token.generate();
        final String jwt = UserJWT.createToken(user, token, EXPIRES);
        AccessToken accessToken = new AccessToken(token.get(), jwt, EXPIRES);

        // Correction: Use your AccessToken class here
        RefreshToken refreshToken = new RefreshToken(Token.generate(), accessToken);

        userToken.add(refreshToken);
        System.out.println(userToken.toString());
        userTokenRepository.save(userToken);
        final Oauth2Response response = Oauth2Response.of(accessToken, refreshToken, EXPIRE_IN, user.getEmail());
        return response;
    }

    public Oauth2Response refreshToken(Oauth2Request request) {
        System.out.println("refresh methode is activated");

        // Correction: Use the appropriate class for validation
        final Set<ConstraintViolation<Oauth2Request>> violations = validator.validate(request, Oauth2Request.RefreshToken.class);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        System.out.println(userTokenRepository.findByRefreshToken(request.getRefreshToken()));
        final UserToken userToken = userTokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new UserNotAuthorizedException());

        final User user = securityService.findBy(userToken.getEmail());
        final Token token = Token.generate();
        final String jwt = UserJWT.createToken(user, token, EXPIRES);
        AccessToken accessToken = new AccessToken(token.get(), jwt, EXPIRES);
        RefreshToken refreshToken = userToken.update((jakarta.security.enterprise.identitystore.openid.AccessToken) accessToken, request.getRefreshToken(), userTokenRepository);
        final Oauth2Response response = Oauth2Response.of(accessToken, refreshToken, EXPIRE_IN, user.getEmail());

        return response;
    }
}