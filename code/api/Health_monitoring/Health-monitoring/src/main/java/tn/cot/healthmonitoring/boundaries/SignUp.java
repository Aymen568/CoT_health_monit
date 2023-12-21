package tn.cot.healthmonitoring.boundaries;

import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.repositories.UserRepository;
import tn.cot.healthmonitoring.utils.Argon2Utility;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.function.Supplier;

@ApplicationScoped
@Path("user")
@Produces({"application/json"})
@Consumes({"application/json"})
public class SignUp {
    private static final Supplier<WebApplicationException> NOT_FOUND = () -> {
        return new WebApplicationException(Status.NOT_FOUND);
    };
    @Inject
    private UserRepository repository;

    public SignUp() {
    }

    @POST
    public Response save(User user) {
        try {
            this.repository.findById(user.getEmail()).orElseThrow();
            return Response.status(Status.UNAUTHORIZED).entity("{\"message\":\"user already exists!!!\"}").build();
        } catch (Exception var6) {
            String password = user.getPassword();
            String passwordhash = Argon2Utility.hash(password.toCharArray());
            User userhash = new User(user.getName(), user.getSurname(), user.getEmail(), passwordhash, user.getRole(), user.getMobile(), user.getEmergency());
            this.repository.save(userhash);
            return Response.ok().entity("{\"username created \":\"" + userhash.getFullname() + "\"}").build();
        }
    }
}