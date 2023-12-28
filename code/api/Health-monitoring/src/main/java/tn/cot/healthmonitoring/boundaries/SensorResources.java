package tn.cot.healthmonitoring.boundaries;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.entities.Sensor;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.exceptions.UserAlreadyExistsException;
import tn.cot.healthmonitoring.filters.Secured;
import tn.cot.healthmonitoring.repositories.SensorRepository;
import tn.cot.healthmonitoring.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Path("sensor")
@Produces({"application/json"})
@Consumes({"application/json"})
public class SensorResources {
    @Inject
    private SensorRepository sensorRepository;
    @Inject
    private UserRepository userRepository;

    /**
     * @return all the vehicles present in the database
     */
    @GET
    @Secured
    @RolesAllowed("ADMIN")
    public List<Sensor> listAll() {
        return sensorRepository.findAll(). collect(Collectors.toList());
    }

    /**
     * @param userId
     * @param sensor
     * @return add a new vehicle by checking if the user is present
     */
    @POST
    @Secured
    @Path("/{userid}")
    public Response addSensor(@PathParam("userid") String userId, Sensor sensor) {
        User user = userRepository.findByEmail(userId).orElse(null);
        if (user != null) {
            sensor.setUserId(user.getEmail());
            sensorRepository.save(sensor);
            return Response.ok(sensor.toString()).build();
        } else {
            return Response.status(404, "User not found").build();
        }
    }

    /**
     * @param userId
     * @return the vehicles attached to a user
     */
    @GET
    @Secured
    @Path("/getbyuser/{userId}")
    public Response getByUserId(@PathParam("userId") String userId) {
        List<Sensor> sensors = sensorRepository.findByUserId(userId);
        return Response.ok(sensors).build();
    }


    /**
     * Get prediction history for a specific user.
     *
     * @param userId The ID of the user
     * @return Response containing the prediction history
     */
    @GET
    @Secured
    @Path("/predictionHistory/{userId}")
    public Response getPredictionHistory(@PathParam("userId") String userId) {
        List<String> predictionHistory = sensorRepository.getPredictionHistory(userId);

        if (predictionHistory != null) {
            return Response.ok(predictionHistory).build();
        } else {
            return Response.status(404, "Prediction history not found for the user").build();
        }
    }
}