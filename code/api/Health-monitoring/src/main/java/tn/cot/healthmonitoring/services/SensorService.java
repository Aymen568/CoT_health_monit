package tn.cot.healthmonitoring.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tn.cot.healthmonitoring.entities.Sensor;
import tn.cot.healthmonitoring.repositories.SensorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SensorService {

    @Inject
    private SensorRepository sensorRepository;

    public void addSensor(Sensor sensor) {
        try {
            // You can include additional business logic here if needed before saving the sensor
            sensorRepository.save(sensor);
        } catch (Exception e) {
            System.out.println("Error somewhere: " + e.getMessage());
            // You might want to handle the exception appropriately (log it, throw a custom exception, etc.)
        }
    }


    public Optional<Sensor> findById(String sensorId) {
        return sensorRepository.findById(sensorId);
    }

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll() .collect(Collectors.toList());
    }


    public void deleteSensorById(String sensorId) {
        sensorRepository.deleteById(sensorId);
    }

    public List<Double> getSensorData(int sensorId) {
        return sensorRepository.findById(sensorId).get().getCollectedValues();
    }

    public List<String> getPredictionHistory(String userId) {
        Optional<Sensor> sensor = sensorRepository.findByUserId(userId).stream().findFirst();
        return sensor.map(value -> value.getLocalMeasurements()
                        .entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue())
                        .collect(Collectors.toList()))
                .orElse(null);
    }


    // Add more methods as needed based on your application requirements
}