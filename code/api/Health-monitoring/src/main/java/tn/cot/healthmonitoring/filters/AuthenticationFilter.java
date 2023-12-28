package tn.cot.healthmonitoring.filters;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Arrays;
import java.util.logging.Logger;

@Provider
@PreMatching // indicate that the filter will be applied globally to all resources
public class AuthenticationFilter implements ContainerRequestFilter {
    // AuthenticationFilter will block requests that do not contain JWT in their header
    public final static String XSS_COOKIE_NAME = "xssCookie";
    // the following paths will later be set to not be blocked by the filter since they represent
    // the signin and signup with oauth pkce paths. JWT can only be obtained after the signin
    public final static String authorizePath = "/api/authorize";
    public final static String authenticatePath = "/api/authenticate/";
    public final static String authenticateadminPath = "/api/authenticateadmin/";
    public final static String tokenPath = "/api/oauth/token";
    public final static String personpath= "/api/user";
    public final static String forgottenpasswordpath= "/api/mail/";
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        // get the access token from the authorization header
        final String path = containerRequestContext.getUriInfo().getRequestUri().getPath(); // get path URI (uniform resource identifier) of the request
        if(path.equals(authorizePath)||path.contains(tokenPath)||path.equals(authenticatePath)||path.equals(personpath)||path.equals(authenticateadminPath)||path.contains(forgottenpasswordpath)){
            return; // if the request path is equal to the signin or signup paths, the request is allowed  without an access token
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // access tokens  are sent in the headers with a prefix of Bearer
            String authenticationToken = authorizationHeader.substring(7);
            // get the access token from the header by taking the part after the prefix Bearer
            if (checkToken(authenticationToken)){
                return;}// Verify the signature  and time validity of token, if they are valid allow the request
            else{containerRequestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Unauthorized to access the resource.")
                    .build());} // if the jwt is not valid, block the request

        }
        //  if the paths do not coresspond and the user did not send a header containing the access token, block the request.
        else{containerRequestContext.abortWith(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Unauthorized access the resource.")
                .build());}


    }
    private static final Config config = ConfigProvider.getConfig();
    // config is used to access variables in microprofile-config.properties
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
    static {
        //acquire the public key which is necessary for the verification of tokens
        FileInputStream fis = null;
        char[] password = config.getValue("jwtSecret",String.class).toCharArray();
        String alias = config.getValue("jwtAlias",String.class);
        PrivateKey pk = null;
        PublicKey pub = null;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            String configDir = System.getProperty("jboss.server.config.dir");
            String keystorePath = configDir + File.separator + "jwt.jks";
            fis = new FileInputStream(keystorePath);
            ks.load(fis, password);
            Key key = ks.getKey("myalis", password);
            if (key instanceof PrivateKey) {
                pk = (PrivateKey) key;
                // Get certificate of public key
                java.security.cert.Certificate cert = ks.getCertificate(alias);
                pub = cert.getPublicKey();
            }
            else {
            logger.severe("The loaded key is not an instance of PrivateKey.");
            }
        } catch (Exception e) {
            logger.severe("Error loading private key from keystore: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                    logger.warning("Error closing keystore file stream: " + ignored.getMessage());
                }
            }
        }
        if (pk == null) {
            logger.severe("Private key is null. Check the keystore loading process.");
        }
        if (pub == null) {
            logger.severe("Public key is null. Check the keystore loading process.");
        }
        privateKey = pk;
        publicKey = pub;
    }
    private static final PrivateKey privateKey;
    static final PublicKey publicKey;
    static final String ISSUER = config.getValue("jwtIssuer",String.class);
    static final String AUDIENCE = config.getValue("jwtAudience",String.class);
    private Boolean checkToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);

            if (!signedJWT.verify(verifier)) {
                logger.warning("JWT signature verification failed.");
                return false;
            }

            JWT jwt = JWTParser.parse(token);
            long currentTime = System.currentTimeMillis(); // get current time

            if (!jwt.getJWTClaimsSet().getIssuer().equals(ISSUER)) {
                logger.warning("JWT issuer mismatch. Expected: " + ISSUER + ", Actual: " + jwt.getJWTClaimsSet().getIssuer());
                return false;
            }

            if (!jwt.getJWTClaimsSet().getAudience().containsAll(Arrays.asList(AUDIENCE))) {
                logger.warning("JWT audience mismatch. Expected: " + AUDIENCE + ", Actual: " + jwt.getJWTClaimsSet().getAudience());
                return false;
            }

            if (jwt.getJWTClaimsSet().getExpirationTime().getTime() <= currentTime) {
                logger.warning("JWT has expired.");
                return false;
            }

            return true;
        } catch (ParseException | JOSEException e) {
            logger.warning("Error during token verification: " + e.getMessage());
            return false;
        }
    }
}