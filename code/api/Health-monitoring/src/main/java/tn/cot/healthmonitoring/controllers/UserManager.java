package tn.cot.healthmonitoring.controllers;

import jakarta.ejb.EJBException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import tn.cot.healthmonitoring.entities.Role;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.repositories.UserRepository;
import tn.cot.healthmonitoring.utils.Argon2Utility;

@Stateless
@LocalBean
public class UserManager {

    @Inject
    private UserRepository userRepository; // Repository to interact with the user database

    public User findByUsername(String email) { // Takes email as input and returns the user
        return userRepository.findById(email).orElseThrow();
    }

    public User authenticate(final String email, final String password) throws EJBException {
        final User user = userRepository.findById(email).orElse(null);

        if (user != null && Argon2Utility.check(user.getPassword(), password.toCharArray())) {
            return user;
        }

        if (user == null) {
            throw new EJBException("Failed sign in with email: " + email + " [Unknown email]");
        } else {
            throw new EJBException("Failed sign in with email: " + email + " [Incorrect password]");
        }
    }

    public User authenticateAdmin(final String email, final String password) throws EJBException {
        final User user = userRepository.findById(email).orElseThrow();

        if (user != null && Argon2Utility.check(user.getPassword(), password.toCharArray()) && user.getRoles().contains(Role.ADMIN)) {
            return user;
        }

        throw new EJBException("Failed sign in with email: " + email + " [Unknown email or wrong password]");
    }
}