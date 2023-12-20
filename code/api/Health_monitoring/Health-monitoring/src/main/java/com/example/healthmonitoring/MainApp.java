package com.example.healthmonitoring;

import com.example.healthmonitoring.entities.Sensor;
import com.example.healthmonitoring.entities.User;
import com.example.healthmonitoring.repositories.SensorRepository;
import com.example.healthmonitoring.repositories.UserRepository;
import jakarta.inject.Inject;

public class MainApp {
    @Inject
    private UserRepository userRepository;
    @Inject
    private SensorRepository sensorRepository;
    public void run() {
        // Creating a new user
        User newUser = new User("John ", "Doe",  "ayml", "aezakmi", Boolean.TRUE, "+21699909203",  "Elamen");
        userRepository.save(newUser);

        // Retrieving users and printing their information
        System.out.println("All Users:");
        userRepository.findAll().forEach(System.out::println);

        // Creating a new sensor
        Sensor newSensor = new Sensor("TemperatureSensor", true);
        sensorRepository.save(newSensor);

        // Retrieving sensors and printing their information
        System.out.println("\nAll Sensors:");
        sensorRepository.findAll().forEach(System.out::println);

        System.out.println("\nSensors with topic 'TemperatureSensor':");
        sensorRepository.findByTopic("TemperatureSensor").forEach(System.out::println);
    }
    public static void main(String[] args) {
        MainApp application = new MainApp();
        application.run();
    }
}
