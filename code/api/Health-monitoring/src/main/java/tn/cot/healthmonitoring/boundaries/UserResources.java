package tn.cot.healthmonitoring.boundaries;


import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.exceptions.UserAlreadyExistsException;
import tn.cot.healthmonitoring.services.UserServiceImpl;

@ApplicationScoped
@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResources {

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
    @Secured
    @RolesAllowed("ADMIN")
    public Response findUsers(){
        System.out.println("find");
        try {
            return Response.ok(userService.findall()).build() ;
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
        System.out.println("signup");
        try {
            return Response.ok(userService.createUser(user)).build() ;
        } catch (UserAlreadyExistsException e){
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
    @Secured
    @RolesAllowed("ADMIN")
    public  Response addUser( @Valid User user){
        try {
            var createdUser = userService.addUser(user);
            return Response.ok(createdUser.getFullname()  + "is added successfully ").build();
        } catch(UserAlreadyExistsException e) {
            return Response.status(400 , e.getMessage()).build() ;

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
    @RolesAllowed("ADMIN")
    public  Response deleteUser(@PathParam("email") String email){
        try {
            userService.delete(email);
            return  Response.ok().build() ;
        }catch(UserNotFoundException e){
            return  Response.status(400 , e.getMessage()).build() ;
        }

    }











}