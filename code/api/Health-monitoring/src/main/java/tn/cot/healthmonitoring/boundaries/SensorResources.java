package tn.cot.healthmonitoring.boundaries;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import tn.cot.healthmonitoring.entities.Sensor;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.exceptions.UserAlreadyExistsException;
import tn.cot.healthmonitoring.repositories.SensorRepository;
import tn.cot.healthmonitoring.repositories.UserRepository;
import tn.cot.healthmonitoring.services.MqttConnection;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Timer;
import java.util.TimerTask;

@ApplicationScoped
@Path("sensor")
@Produces({"application/json"})
@Consumes({"application/json"})
public class SensorResources {
    private final AtomicLong sensorIdCounter = new AtomicLong(1);
    @Inject
    private SensorRepository sensorRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private MqttConnection mqttConnection;
    @Inject
    @ConfigProperty(name = "data.collection.interval", defaultValue = "1000")
    private Long dataCollectionInterval;

    /**
     * @return all the vehicles present in the database
     */
    @GET
    //@Secured
    //@RolesAllowed("ADMIN")
    public List<Sensor> listAll() {
        return sensorRepository.findAll(). collect(Collectors.toList());
    }

    /**
     * @param userId
     * @param location
     * @return add a new sensor by checking if the user is present
     */
    @POST
    @Path("/{userId}/{location}")
    public Response addSensor(@PathParam("userId") String userId ,@PathParam("location")String location) {
        User user = userRepository.findByEmail(userId).orElse(null);
        System.out.println(userId);
        if (user != null) {
            long sensorId = sensorIdCounter.getAndIncrement();
            System.out.println(user.getFullname());
            System.out.println("Generated Sensor ID: " + sensorId);
            Sensor sensor = new Sensor(sensorId,userId, location);
            sensorRepository.save(sensor);
            return Response.ok(sensor).build();
        } else {
            return Response.status(404, "User not found").build();
        }
    }
    /**
     * @param userId
     * @return the vehicles attached to a user
     */
    @GET
    //@Secured
    @Path("/{userId}")
    public Response getByUserId(@PathParam("userId")String userId) {
        List<Sensor> sensors = sensorRepository.findByUserId(userId);
        return Response.ok(sensors).build();
    }


    /**
     * Get prediction history for a specific user.
     *
     * @param userId The ID of the user
     * @return Response containing the prediction history
     */


        // --------------------------------- Data part -------------------------------------------------//
    @GET
    //@Secured
    @Path("/predictionHistory/{userId}")
    public Response getPredictionHistory(@PathParam("userId") String userId) {
        List<String> predictionHistory = sensorRepository.getPredictionHistory(userId);

        if (predictionHistory != null) {
            return Response.ok(predictionHistory).build();
        } else {
            return Response.status(404, "Prediction history not found for the user").build();
        }
    }

    @GET
    @Path("/visualize/{sensorId}")
    public Response visualizeSensorData(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId).orElse(null);
        System.out.println("Generated Sensor ID: " + sensorId);
        if (sensor != null) {
            List<Double> sensorData = sensor.getCollectedValues();
            return Response.ok(sensorData).build();
        } else {
            return Response.status(404, "Sensor not found").build();
        }
    }


    @POST
    @Path("/start/{sensorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startMeasurement(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId).orElse(null);

        if (sensor != null) {
            sensor.startMeasurement();
            return Response.ok("Measurement started for sensor: " + sensorId).build();
        } else {
            return Response.status(404, "Sensor not found").build();
        }
    }

    private void collectValue(Sensor sensor, Double newValue) {
        // Add collected data to the sensor
        sensor.collectValue(newValue);
    }


    @POST
    @Path("/stop/{sensorId}")
    public Response stopMeasurement(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId).orElse(null);

        if (sensor != null) {
            sensor.stopMeasurement();
            return Response.ok("Measurement stopped for sensor: " + sensorId).build();
        } else {
            return Response.status(404, "Sensor not found").build();
        }
    }

}