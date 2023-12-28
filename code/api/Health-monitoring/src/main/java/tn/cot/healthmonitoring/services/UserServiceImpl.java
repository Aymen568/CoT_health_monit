package tn.cot.healthmonitoring.services;



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tn.cot.healthmonitoring.entities.Role;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.exceptions.*;
import tn.cot.healthmonitoring.repositories.UserRepository;
import tn.cot.healthmonitoring.utils.Argon2Utility;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static tn.cot.healthmonitoring.entities.Role.CLIENT;


@ApplicationScoped
public class UserServiceImpl implements UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    Argon2Utility argon2Utility ;

    /**
     *
     * @param user
     * @return User
     * @apiNote  THis methode  is used  to create Admin account
     */

    public User createUser(User user) throws UserAlreadyExistsException {
        user.setRoles(Set.of(Role.CLIENT));
        if ( user.getEmail() == null || user.getFullname() == null || user.getPassword() == null || user.getMobile() ==null || user.getEmergency() == null) {
            // Throw an exception or handle the null values appropriately
            throw new IllegalArgumentException("User data cannot be null");
        }

        // Check if the user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        System.out.println("setting password");
        // Set the hashed password
        System.out.println("going to create a user");

        // Save the user
        User createdUser = userRepository.save(user);

        // Log or print the created user for debugging
        System.out.println("Created user: " + createdUser);

        return createdUser;
    }



    public Stream<User> findall(){
        return userRepository.findAll();
    }

    /**
     *
     * @param user
     * @return User
     * @throws UserAlreadyExistsException
     * @apiNote  This methode is  used   when the admin add some users to maintain  racks
     */
    @Override
    public User addUser(User user)  {
        if(userRepository.findById(user.getEmail()).isPresent()){
            System.out.println( "email"+ user.getEmail() + "already present");
            throw new UserAlreadyExistsException(user.getEmail() +" already exists") ;
        }
        user.updatePassword(user.getPassword(),argon2Utility);
        return userRepository.save(user) ;

    }
    @Override
    public void updatePassword(String email, String newPassword) throws UserNotFoundException {
        // Retrieve the user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional == null) {
            throw new UserNotFoundException("User with email " + email + " not found");

        }
        else {
        User user = userOptional.get();
        user.updatePassword(newPassword, argon2Utility);
        userRepository.save(user);}
    }

    /**
     *
     * @param email
     * @throws  UserNotFoundException
     * @apiNote this methode used by the admin to delete users
     */
    @Override
    public void delete(String email)  {
        if(!userRepository.findById(email).isPresent()){
            throw new UserNotFoundException("there is  no user with email :"+email) ;
        }
        userRepository.deleteById(email);
    }
    // Existing methods...

    /**
     * @param email
     * @return User
     * @apiNote This method is used to retrieve a user by email.
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

}