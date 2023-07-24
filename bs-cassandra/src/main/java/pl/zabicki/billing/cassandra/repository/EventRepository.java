package pl.zabicki.billing.cassandra.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.zabicki.billing.cassandra.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {
    List<Event> findByClientIdAndAccountId(String clientId, String accountId);
}
