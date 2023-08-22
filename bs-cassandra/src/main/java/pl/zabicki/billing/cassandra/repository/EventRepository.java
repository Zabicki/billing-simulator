package pl.zabicki.billing.cassandra.repository;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import org.springframework.data.cassandra.repository.Consistency;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.zabicki.billing.cassandra.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {
    //@Consistency(DefaultConsistencyLevel.ONE)
    @Query("SELECT * FROM Event WHERE clientId = ?0 AND accountId = ?1")
    List<Event> findByClientIdAndAccountId(String clientId, String accountId);
    @Query("SELECT COUNT(*) FROM event")
    long countAll();

    @Query("TRUNCATE TABLE event")
    void truncate();
}
