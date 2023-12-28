package tn.cot.healthmonitoring.services;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import tn.cot.healthmonitoring.repositories.SensorRepository;
import tn.cot.healthmonitoring.repositories.UserRepository;

import javax.net.ssl.SSLSocketFactory;

@Singleton
@Startup
public class MqttConnection {

    private static final Config config = ConfigProvider.getConfig();
    private final String uri = config.getValue("mqtt.uri", String.class);
    private final String username = config.getValue("mqtt.username", String.class);
    private final String password = config.getValue("mqtt.password", String.class);

    @Inject
    private SensorRepository sensorRepository;

    @Inject
    private UserRepository userRepository;

    @PostConstruct
    public void start() {
        try {
            System.out.println("\n --------------------------------------------------- \n");
            System.out.println("MQTT HAS BEEN STARTED");
            System.out.println("\n --------------------------------------------------- \n");
            System.out.println(uri);
            // CLIENT CONNECTION OPTIONS
            MqttClient client = new MqttClient(uri, MqttClient.generateClientId(), new MemoryPersistence());
            System.out.println("----------------------------------------------");
            System.out.println(client.getClientId());
            System.out.println("----------------------------------------------");

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            //mqttConnectOptions.setUserName(username);
            //mqttConnectOptions.setPassword(password.toCharArray());
            //mqttConnectOptions.setSocketFactory(SSLSocketFactory.getDefault());
            mqttConnectOptions.setKeepAliveInterval(120);
            mqttConnectOptions.setConnectionTimeout(120);
            mqttConnectOptions.setAutomaticReconnect(true);
            client.connect(mqttConnectOptions);

            // Set up callback
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("\n --------------------------------------------------- \n");
                    System.out.println("CLIENT LOST CONNECTION " + cause);
                    System.out.println("\n --------------------------------------------------- \n");
                    try {
                        client.reconnect();
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws MqttException {
                    System.out.println("We are under message Arrived ");
                    System.out.println("\n-----------------------------------------------\n");
                    System.out.println(topic);
                    System.out.println("\n-----------------------------------------------\n");

                    // Todo: Implement logic for saving data to the database and notifying users
                    if (topic.equals("health")) {
                        System.out.println("health: " + message + " is successfully added");
                    }

                    if (topic.equals("clients/clientid")) {
                        String userId = new String(message.getPayload());
                        System.out.println(userId);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("\n --------------------------------------------------- \n");
                    System.out.println("delivery complete " + token);
                }
            });

            client.subscribe("health", 1);
            client.subscribe("clients/clientid", 1);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}