package tn.cot.healthmonitoring.boundaries;
// ModelResources.java
package tn.cot.healthmonitoring.boundaries;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import tn.cot.healthmonitoring.models.Model;
import tn.cot.healthmonitoring.services.ModelService;

@Path("model")
@Produces({"application/json"})
@Consumes({"application/json"})
public class ModelResources {

    @Inject
    private ModelService modelService;

    /**
     * Endpoint to train the model.
     *
     * @return Response
     */
    @POST
    @Path("train")
    public Response trainModel() {
        modelService.trainModel();
        return Response.ok().build();
    }

    /**
     * Endpoint to predict using the trained model.
     *
     * @param input Input for prediction
     * @return Prediction result
     */
    @POST
    @Path("predict")
    public Response predict(Model input) {
        String prediction = modelService.predict(input);
        return Response.ok(prediction).build();
    }

    // Add more endpoints as needed for your application

}