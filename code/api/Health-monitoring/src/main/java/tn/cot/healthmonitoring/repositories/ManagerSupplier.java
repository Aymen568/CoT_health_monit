package tn.cot.healthmonitoring.repositories;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import java.util.function.Supplier;
import org.eclipse.jnosql.communication.Settings;
import org.eclipse.jnosql.databases.mongodb.communication.MongoDBDocumentConfiguration;
import org.eclipse.jnosql.databases.mongodb.communication.MongoDBDocumentManager;
import org.eclipse.jnosql.databases.mongodb.communication.MongoDBDocumentManagerFactory;

@Alternative
@Priority(2000)
@ApplicationScoped
public class ManagerSupplier implements Supplier<MongoDBDocumentManager> {
    public ManagerSupplier() {
    }

    @Produces
    public MongoDBDocumentManager get() {
        Settings settings = Settings.builder().build();
        MongoDBDocumentConfiguration configuration = new MongoDBDocumentConfiguration();
        MongoDBDocumentManagerFactory factory = configuration.apply(settings);
        return factory.apply("database");
    }
}
