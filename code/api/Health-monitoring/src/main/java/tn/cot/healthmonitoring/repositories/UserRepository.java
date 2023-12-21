package tn.cot.healthmonitoring.repositories;

import  tn.cot.healthmonitoring.entities.User;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Stream<User> findAll();

    Stream<User> findBypermissionLevel(Long var1);

    Stream<User> findByfullnameIn(String var1);
}
