package tn.cot.healthmonitoring.boundaries;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.repositories.UserRepository;
import tn.cot.healthmonitoring.utils.Argon2Utility;


import java.util.function.Supplier;
@ApplicationScoped
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SignUpResource {
    private static final Supplier<WebApplicationException> NOT_FOUND =
            () -> new WebApplicationException(Response.Status.NOT_FOUND);
    @Inject
    private UserRepository repository;
    @POST // Post method that receives User credentials from sign up in JSON format and saves it in the database
    public Response save(User user) {
        if (user.getEmail()==null || user.getFullname()==null || user.getPassword()==null || user.getMobile()==null || user.getEmergency()==null){
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Empty credentials, please enter your credentials")
                    .build();
        }
        try {
            repository.findById(user.getEmail()).orElseThrow(); // If User already exists , the request cannot go through
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\":\"user already exists!!!\"}").build();
        } catch (Exception e) {
            String password = user.getPassword();
            String passwordhash = Argon2Utility.hash(password.toCharArray()); // Hash the password tapped by the user before saving it in the database
            User userhash = new User(user.getEmail(), user.getFullname(), password, user.getMobile(), user.getEmergency()); //create new User entity with the new hashed password
            repository.save(userhash); // save the data in MongoDB
            return Response.ok().entity("{\"username created \":\"" + userhash.getFullname() + "\"}").build();
        }


    }
}