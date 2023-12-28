package tn.cot.healthmonitoring.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class DataService {

    @Inject
    private SensorService sensorService;

    @Context
    private SecurityContext securityContext;

    private boolean isUserAuthenticated() {
        // Check if the user is authenticated using the injected SecurityContext
        return securityContext != null && securityContext.isUserInRole("USER");
    }

    public void endMeasurementAndSendData(Integer sensorId) {
        // Check if the user is authenticated
        if (isUserAuthenticated()) {
            List<Double> collectedValues = sensorService.getSensorData(sensorId);

            if (!collectedValues.isEmpty()) {
                System.out.println("Measurement Ended at Timestamp: " + LocalDateTime.now());
                System.out.println("Collected Values: " + collectedValues);

            } else {
                System.out.println("No data collected for sensor: " + sensorId);
            }
        } else {
            System.out.println("Unauthorized access to send data");

        }
    }

}