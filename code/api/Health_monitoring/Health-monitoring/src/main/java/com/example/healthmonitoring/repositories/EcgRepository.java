package com.example.healthmonitoring.repositories;

import com.example.healthmonitoring.entities.Ecg;
import jakarta.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface EcgRepository  extends CrudRepository<Ecg, String> { // repository containing the methods for interacting with SensorDB entity in mongodb
    Stream<Ecg> findAll();
    Stream<Ecg> findByid(int id);
}
