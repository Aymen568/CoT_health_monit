package tn.cot.healthmonitoring.repositories;

import jakarta.data.repository.CrudRepository;
import tn.cot.healthmonitoring.entities.Displayer;

import java.util.stream.Stream;

public interface DisplayerRepository extends CrudRepository<Displayer, String> {
    Stream<Displayer> findAll();

    Stream<Displayer> findByid(int var1);
}
