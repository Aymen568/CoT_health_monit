package tn.cot.healthmonitoring.boundaries;

import tn.cot.healthmonitoring.entities.Sensor;
import tn.cot.healthmonitoring.repositories.SensorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("sensor")
@Produces({"application/json"})
@Consumes({"application/json"})
public class SensorEndpoints {
    private static final Supplier<WebApplicationException> NOT_FOUND = () -> {
        return new WebApplicationException(Status.NOT_FOUND);
    };
    @Inject
    private SensorRepository repository;

    public SensorEndpoints() {
    }

    @GET
    public List<Sensor> findAll() {
        return (List)this.repository.findAll().collect(Collectors.toList());
    }

    @POST
    public void save(Sensor sensor) {
        this.repository.save(sensor);
    }
}

