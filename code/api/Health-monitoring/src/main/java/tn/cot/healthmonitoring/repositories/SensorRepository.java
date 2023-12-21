package tn.cot.healthmonitoring.repositories;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import tn.cot.healthmonitoring.entities.Sensor;
import tn.cot.healthmonitoring.entities.User;

import java.util.stream.Stream;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, String> {
    Stream<Sensor> findAll();

    Stream<User> findByTopic(String var1);

    Stream<User> findByid(int var1);
}
