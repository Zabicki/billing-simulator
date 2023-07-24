package pl.zabicki.billing.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zabicki.billing.postgres.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByClientIdAndAccountId(String clientId, String accountId);
}
