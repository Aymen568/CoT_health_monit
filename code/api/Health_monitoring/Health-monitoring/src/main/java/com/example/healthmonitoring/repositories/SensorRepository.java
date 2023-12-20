package com.example.healthmonitoring.repositories;

import com.example.healthmonitoring.entities.Sensor;
import com.example.healthmonitoring.entities.User;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

import java.util.stream.Stream;
@Repository

public interface SensorRepository  extends CrudRepository<Sensor, String> { // repository containing the methods for interacting with SensorDB entity in mongodb
    Stream<Sensor> findAll();
    Stream<User> findByTopic(String T);
    Stream<User> findByid(int id);
}

