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

    }

    public static void main(String[] args) {
        MainApp application = new MainApp();
        application.run();
    }
}
