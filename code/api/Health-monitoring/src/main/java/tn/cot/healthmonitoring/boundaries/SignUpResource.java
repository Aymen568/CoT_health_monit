package tn.cot.healthmonitoring.boundaries;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.repositories.UserRepository;
import tn.cot.healthmonitoring.utils.Argon2Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        if (user.getEmail() == null || user.getFullname() == null || user.getPassword() == null || user.getMobile() == null || user.getEmergency() == null) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Empty credentials, please enter your credentials")
                    .build();
        }
        try {
            System.out.println("Received request for user: " + user.getEmail());
            Optional<User> existingUser = repository.findById(user.getEmail());
            if (existingUser.isPresent()) {
                System.out.println("User with the same email already exists!");
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\":\"User with the same email already exists!!!\"}")
                        .build();
            }
            String password = user.getPassword();
            String passwordhash = Argon2Utility.hash(password.toCharArray()); // Hash the password tapped by the user before saving it in the database
            User userhash = new User(user.getEmail(), user.getFullname(), password, user.getMobile(), user.getEmergency()); //create new User entity with the new hashed password
            repository.save(userhash); // save the data in MongoDB
            return Response.ok().entity("{\"username created \":\"" + userhash.getFullname() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("another unkonw error")
                    .build();
        }


    }
    /**
     * Get normal and abnormal values for a specific user.
     *
     * @param userId The ID of the user
     * @return Response containing normal and abnormal values
     */
    @GET
    @Path("/getvalues/{userId}")
    public Response getValues(@PathParam("userId") String userId) {
        User user = repository.findById(userId).orElse(null);
        System.out.println("fetching user");


        if (user == null) {
            return Response.status(404, "User not found").build();
        }
        long normalValues = user.getNormal();
        long abnormalValues = user.getUbnormal();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Normal", normalValues);
        responseMap.put( "Abnormal", abnormalValues);

        return Response.ok(responseMap).build();
    }
    /**
     * Set normal or abnormal values for a specific user.
     *
     * @param userId The ID of the user
     * @param normal   Boolean indicating whether to set normal or abnormal values
     * @return JSON response indicating success or failure
     */
    @PUT
    @Path("/setvalues/{userId}")
    public Response setValues(@PathParam("userId") String userId, Boolean normal) {
        User user = repository.findById(userId).orElse(null);

        if (user == null) {
            return Response.status(404, "User not found").build();
        }

        if (normal) {
            user.setNormal();  // Assuming setNormal method takes no parameters
        } else {
            user.setUbnormal();  // Assuming setUbnormal method takes no parameters
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "Values set successfully");
        return Response.ok(responseMap).build();
    }




}