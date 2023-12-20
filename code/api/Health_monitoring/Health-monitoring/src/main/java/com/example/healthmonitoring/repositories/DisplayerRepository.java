package com.example.healthmonitoring.repositories;

import com.example.healthmonitoring.entities.Displayer;
import jakarta.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface DisplayerRepository extends CrudRepository<Displayer, String> { // repository containing the methods for interacting with SensorDB entity in mongodb
    Stream<Displayer> findAll();
    Stream<Displayer> findByid(int id);
}
