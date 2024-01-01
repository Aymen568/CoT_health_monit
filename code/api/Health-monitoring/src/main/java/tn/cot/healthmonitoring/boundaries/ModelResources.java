package tn.cot.healthmonitoring.boundaries;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@ApplicationScoped
@Path("model")
@Produces({"application/json"})
@Consumes({"application/json"})
public class ModelResources {

}
