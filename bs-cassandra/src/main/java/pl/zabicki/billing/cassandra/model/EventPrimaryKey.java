package pl.zabicki.billing.cassandra.model;


import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

//@PrimaryKeyClass
//@AllArgsConstructor
public class EventPrimaryKey {

    /*@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    String clientId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    String accountId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    String id;*/
}
