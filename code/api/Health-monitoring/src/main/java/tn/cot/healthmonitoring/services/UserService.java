package tn.cot.healthmonitoring.services;

import tn.cot.healthmonitoring.exceptions.UserAlreadyExistsException;
import tn.cot.healthmonitoring.exceptions.UserNotFoundException;
import tn.cot.healthmonitoring.entities.User;

public interface UserService {

    User createUser(User user) throws UserAlreadyExistsException;
    User addUser(User user) throws  UserAlreadyExistsException  ;

    User getUserByEmail(String email);
    void delete(String email) throws UserNotFoundException;

    void updatePassword(String email, String newPassword) throws UserNotFoundException;


}