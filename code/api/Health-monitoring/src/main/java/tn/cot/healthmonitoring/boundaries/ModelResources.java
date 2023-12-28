package tn.cot.healthmonitoring.boundaries;
// ModelResources.java
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.entities.Model;
import tn.cot.healthmonitoring.services.ModelService;

import java.util.List;

@Path("model")
@Produces({"application/json"})
@Consumes({"application/json"})
public class ModelResources {

    @Inject
    private ModelService modelService;
    /**
     * Endpoint to predict using the trained model.
     *
     * @param input Input for prediction
     * @return Prediction result
     */
    @POST
    @Path("/predict")
    public Response predict(List<Double> input) {
        try {
            int prediction = modelService.predict(input);
            return Response.ok("Prediction: " + prediction).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Model has not been trained yet.")
                    .build();
        }
    }

    // Add more endpoints as needed for your application

}