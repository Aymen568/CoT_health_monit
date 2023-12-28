package tn.cot.healthmonitoring.services;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tn.cot.healthmonitoring.entities.Role;
import tn.cot.healthmonitoring.entities.User;
import tn.cot.healthmonitoring.exceptions.UserAlreadyExistsException;
import tn.cot.healthmonitoring.utils.Argon2Utility;

import java.util.Set;

@ApplicationScoped
public class InitializationService {

    @Inject
    private UserServiceImpl userService;

    @Inject
    private Argon2Utility argon2Utility; // Inject the Argon2Utility

    @PostConstruct
    public void initialize() {
        // Check if admin user exists, if not, create one during application startup
        User admin = userService.getUserByEmail("aymenabidi682@gmail.com");
        if (admin == null) {
            // Create the admin user
            admin = new User();
            admin.setEmail("aymenabidi682@gmail.com");
            admin.setPassword("admin");
            admin.setRoles(Set.of(Role.ADMIN));

            // You might want to set other properties like fullname, phone, etc.

            // Add the admin user to the system
            try {
                userService.createUser(admin);
            } catch (UserAlreadyExistsException e) {
                // Handle the exception if the user somehow already exists (shouldn't happen)
                e.printStackTrace();
            }
        }
    }
}