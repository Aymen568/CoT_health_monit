package tn.cot.healthmonitoring.repositories;

import jakarta.data.repository.CrudRepository;
import tn.cot.healthmonitoring.entities.Ecg;

import java.util.stream.Stream;

public interface EcgRepository extends CrudRepository<Ecg, String> {
    Stream<Ecg> findAll();

    Stream<Ecg> findByid(int var1);
}
