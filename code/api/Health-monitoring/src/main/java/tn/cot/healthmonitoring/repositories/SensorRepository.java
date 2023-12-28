package tn.cot.healthmonitoring.repositories;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import tn.cot.healthmonitoring.entities.Sensor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, String> {
    Stream<Sensor> findAll();

    List<Sensor> findByUserId(String userid);

    Optional<Sensor> findById(int var1);
    List<String> getPredictionHistory(String userId);


}
