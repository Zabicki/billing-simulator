package pl.zabicki.billing.cassandra;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/*
@Configuration
@EnableCassandraRepositories(basePackages = "pl.zabicki.billing.cassandra.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "eventstore";
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification
                        .createKeyspace("eventstore")
                        .ifNotExists()
                // You can add additional specifications like replication strategy here
        );
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"pl.zabicki.billing.cassandra.model"};
    }

    */
/*@Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer() throws IOException {
        File f = new File("/home/chris/Work/IdeaProjects/billing-simulator/bs-cassandra/src/main/resources/application.conf");
        String s = Files.readString(Path.of("/home/chris/Work/IdeaProjects/billing-simulator/bs-cassandra/src/main/resources/application.conf"));
        return (builder) -> builder.withConfigLoader(DriverConfigLoader.fromFile(f));
    }*//*


}
*/
