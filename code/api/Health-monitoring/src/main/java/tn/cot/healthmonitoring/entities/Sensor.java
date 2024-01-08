package tn.cot.healthmonitoring.entities;

import jakarta.json.bind.annotation.JsonbVisibility;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import tn.cot.healthmonitoring.utils.FieldPropertyVisibilityStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity("sensor")
@JsonbVisibility(FieldPropertyVisibilityStrategy.class)
public class Sensor {
    @Id
    private long id;
    @Column
    private List<Double> collectedValues;

    @Column("userId")
    private String userId;
    @Column
    private String location;
    @Column
    private Boolean state;



    // Map to store local measurements
    private Map<LocalDateTime, AbstractMap.SimpleEntry<List<Double>, Integer>> localMeasurements;

    public Sensor() {
        // Initialize the map in the constructor
        this.localMeasurements = new HashMap<>();
        this.collectedValues = new ArrayList<>();
        state = false;
    }

    public void startMeasurement() {
        state = true;
    }


    public void stopMeasurement() {
        state = false;
    }
    public Boolean isMeasurementStarted(){return this.state;}
    public Sensor(long sensorid,String id,String location) {
        this.id = sensorid;
        this.userId = id;
        this.collectedValues = new ArrayList<>();
        this.localMeasurements = new HashMap<>();
        this.location = location;
    }
    public long getId() {
        return this.id;
    }

    public void collectValue(Double newValue) {
        this.collectedValues.add(newValue);
    }

    public List<Double> getCollectedValues() {
        return new ArrayList<>(collectedValues); // Return a copy to avoid external modification.
    }

    public void clearCollectedValues() {
        this.collectedValues.clear();
    }

    public void finishMeasurement(Integer prediction) {
        LocalDateTime timestamp = LocalDateTime.now();
        System.out.println("Timestamp: " + timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("Finishingm mesure");
        AbstractMap.SimpleEntry<List<Double>, Integer> measurementEntry =
                new AbstractMap.SimpleEntry<>(new ArrayList<>(collectedValues), prediction);
        localMeasurements.put(timestamp, measurementEntry);
        clearCollectedValues();
    }

    public Map<LocalDateTime, AbstractMap.SimpleEntry<List<Double>, Integer>> getLocalMeasurements() {
        return new HashMap<>(localMeasurements); // Return a copy to avoid external modification.
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sensor)) {
            return false;
        }
        Sensor sensor = (Sensor) o;
        return Objects.equals(id, sensor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, collectedValues, localMeasurements);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorid='" + id + '\'' +
                ", userid=" + userId +
                ", location=" + location +
                '}';
    }


}