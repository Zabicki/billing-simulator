package pl.zabicki.billing.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import pl.zabicki.billing.cassandra.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends CassandraRepository<Event, String> {
    @Query("SELECT * FROM Event WHERE clientId = ?0 AND accountId = ?1")
    List<Event> findByClientIdAndAccountId(String clientId, String accountId);
    @Query("SELECT COUNT(*) FROM event")
    long countAll();

    @Query("TRUNCATE TABLE event")
    void truncate();
}
