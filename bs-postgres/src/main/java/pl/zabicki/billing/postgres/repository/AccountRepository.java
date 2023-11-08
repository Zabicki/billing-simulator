package pl.zabicki.billing.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.zabicki.billing.postgres.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE account RESTART IDENTITY", nativeQuery = true)
    void truncate();
}
