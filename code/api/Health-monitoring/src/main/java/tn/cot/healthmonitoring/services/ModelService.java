package tn.cot.healthmonitoring.services;

import jakarta.enterprise.context.ApplicationScoped;
import tn.cot.healthmonitoring.entities.Model;

import java.time.LocalDateTime;
import java.util.List;
import org.antlr.v4.runtime.misc.Pair;

@ApplicationScoped
public class ModelService {

    private Model trainedModel;


    public int predict(List< Double> input) {
        if (trainedModel == null) {
            throw new IllegalStateException("Model has not been trained yet.");
        }

        // Implement the logic to make predictions using the trained model
        // Example: int prediction = trainedModel.predict(input);
        return 42;  // Placeholder, replace with your actual prediction logic
    }

    // Add more methods as needed for your application

}