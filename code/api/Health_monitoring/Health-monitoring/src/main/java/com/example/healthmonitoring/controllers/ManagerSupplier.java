package com.example.healthmonitoring.controllers;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;
import org.eclipse.jnosql.communication.Settings;
import org.eclipse.jnosql.databases.mongodb.communication.MongoDBDocumentConfiguration;
import org.eclipse.jnosql.databases.mongodb.communication.MongoDBDocumentManager;
import org.eclipse.jnosql.databases.mongodb.communication.MongoDBDocumentManagerFactory;

import java.util.function.Supplier;
// responsible for managing interactions with the database
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
@ApplicationScoped
public class ManagerSupplier implements Supplier<MongoDBDocumentManager> {

    @Produces
    public MongoDBDocumentManager get() {
        Settings settings = Settings.builder().build();
        MongoDBDocumentConfiguration configuration = new MongoDBDocumentConfiguration();
        MongoDBDocumentManagerFactory factory = configuration.apply(settings);
        return factory.apply("database");
    }
}
