package pl.zabicki.billing.postgres.repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.zabicki.billing.postgres.model.Event;

import java.util.List;
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByClientIdAndAccountId(String clientId, String accountId);

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE event RESTART IDENTITY", nativeQuery = true)
    void truncate();
}
