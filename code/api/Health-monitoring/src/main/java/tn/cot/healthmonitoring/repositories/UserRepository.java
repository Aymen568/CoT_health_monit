package tn.cot.healthmonitoring.repositories;

import  tn.cot.healthmonitoring.entities.User;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Stream<User> findAll();

    Stream<User> findBypermissionLevel(Long var1);

    Stream<User>findByfullname(String var1);
    Optional<User> findByEmail(String email ) ;

    void update(User user);
}
