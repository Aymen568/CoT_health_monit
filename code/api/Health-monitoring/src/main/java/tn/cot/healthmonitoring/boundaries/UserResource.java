package tn.cot.healthmonitoring.boundaries;


import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.exceptions.UserAlreadyExistsException;
import tn.cot.healthmonitoring.exceptions.UserNotFoundException;

import tn.cot.healthmonitoring.services.UserServiceImpl;
import tn.cot.healthmonitoring.utils.Argon2Utility;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("")
@Produces({"application/json"})
@Consumes({"application/json"})
public class UserResource {

    @Inject
    private UserServiceImpl userService ;


    /**
     *
     * @param
     * @return Response entity
     * @throws UserAlreadyExistsException
     * @apiNote : used to  create admin account
     */
    @GET
    @Path("/find")
    //@RolesAllowed("ADMIN")
    public Response findUsers(){
        System.out.println("find");
        try {
            List<User> userList = userService.findall().collect(Collectors.toList());
            return Response.ok(userList).build() ;
        } catch (UserAlreadyExistsException e){
            return  Response.status(400, e.getMessage()).build();
        }
    }
    /**
     *
     * @param user
     * @return Response entity
     * @throws  UserAlreadyExistsException
     * @apiNote : used to  create admin account
     */
    @POST
    @Path("/signup")
    public Response createUser(@Valid User user){

        try {
            return Response.ok(userService.createUser(user)).build() ;
        } catch (UserAlreadyExistsException e){
            System.out.println("user already exsits");
            return  Response.status(400, e.getMessage()).build();
        }
    }

    /**
     *
     * @param user
     * @return status
     * @apiNote  this methode is used by the admin to add users
     */

    @POST()
    @Path("user/add")

    public Response addUser(@Valid User user) {
        try {
            var createdUser = userService.addUser(user);

            // Check if createdUser or its properties are null before using them
            if (createdUser != null && createdUser.getFullname() != null) {
                String successMessage = createdUser.getFullname() + " is added successfully ";
                return Response.ok(successMessage).build();

            } else {
                // Log an error message
                System.err.println("Failed to add user. Check the data.");

                // Return a 500 Internal Server Error response
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to add user. Check the data.")
                        .build();
            }
        } catch (UserAlreadyExistsException e) {
            // Log the exception
            System.err.println("User creation failed: " + e.getMessage());

            // Return a 400 Bad Request response for user already exists scenario
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    /**
     *
     * @param email
     * @return status
     * @apiNote  this  methode is used by the Admin to delete users
     */

    @DELETE()
    @Path("user/{email}")
    public  Response deleteUser(@PathParam("email") String email){
        try {
            userService.delete(email);
            return  Response.ok().build() ;
        }catch(UserNotFoundException e){
            return  Response.status(400 , e.getMessage()).build() ;
        }

    }

    @PATCH
    @Path("/user/updatePassword/{email}")
    public Response updatePassword(@PathParam("email") String email, String newPassword) {
        try {
            userService.updatePassword(email, newPassword);
            return Response.ok("Password updated successfully").build();
        } catch (UserNotFoundException e) {
            return Response.status(404, e.getMessage()).build();
        }
    }

    @POST
    @Path("/user/forgetPassword/{email}")
    public Response forgetPassword(@PathParam("email") String email, String oldPassword, String newPassword) {
        User user = userService.getUserByEmail(email);

        Argon2Utility argon2Utility;

        if (user == null) {
            return Response.status(404, "No such user with this email").build();
        }

        String hashedOldPassword = Argon2Utility.hash(oldPassword.toCharArray());

        System.out.println("Stored Hashed Password: " + user.getPassword());
        System.out.println("Hashed Old Password: " + hashedOldPassword);

        if (!Argon2Utility.check(user.getPassword(), oldPassword.toCharArray())) {
            return Response.status(404, "wrong password, try again please").build();
        }

        userService.updatePassword(email, newPassword);
        return Response.ok("Password updated successfully").build();

    }

    /**
     * Get user information by email.
     *
     * @param email The email of the user.
     * @return Response entity.
     */
    @GET
    @Path("/{email}")
    public Response getUserByEmail(@PathParam("email") String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user != null) {
                return Response.ok(user).build();
            } else {
                return Response.status(404, "User not found").build();
            }
        } catch (Exception e) {
            return Response.status(500, "Internal Server Error").build();
        }
    }



}