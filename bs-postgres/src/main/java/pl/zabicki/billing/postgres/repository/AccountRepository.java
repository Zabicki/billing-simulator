package pl.zabicki.billing.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zabicki.billing.postgres.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByClientId(String clientId);
}
