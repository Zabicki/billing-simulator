package pl.zabicki.billing.cassandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import pl.zabicki.billing.cassandra.model.Account;

@Repository
public interface AccountRepository extends CassandraRepository<Account, String> {
}
