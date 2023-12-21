package tn.cot.healthmonitoring;


import tn.cot.healthmonitoring.entities.Sensor;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.repositories.SensorRepository;
import tn.cot.healthmonitoring.repositories.UserRepository;
import jakarta.inject.Inject;
import java.io.PrintStream;
import java.util.Objects;
import java.util.stream.Stream;

public class MainApp {
    @Inject
    private UserRepository userRepository;
    @Inject
    private SensorRepository sensorRepository;

    public MainApp() {
    }

    public void run() {
        User newUser = new User("John ", "Doe", "ayml", "aezakmi", Boolean.TRUE, "+21699909203", "Elamen");
        this.userRepository.save(newUser);
        System.out.println("All Users:");
        Stream var10000 = this.userRepository.findAll();
        PrintStream var10001 = System.out;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::println);
        Sensor newSensor = new Sensor("TemperatureSensor", true);
        this.sensorRepository.save(newSensor);
        System.out.println("\nAll Sensors:");
        var10000 = this.sensorRepository.findAll();
        var10001 = System.out;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::println);
        System.out.println("\nSensors with topic 'TemperatureSensor':");
        var10000 = this.sensorRepository.findByTopic("TemperatureSensor");
        var10001 = System.out;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::println);
    }

    public static void main(String[] args) {
        MainApp application = new MainApp();
        application.run();
    }
}
