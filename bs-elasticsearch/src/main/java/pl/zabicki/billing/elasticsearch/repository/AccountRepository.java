package pl.zabicki.billing.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import pl.zabicki.billing.elasticsearch.model.Account;

public interface AccountRepository extends ElasticsearchRepository<Account, String> {
}
